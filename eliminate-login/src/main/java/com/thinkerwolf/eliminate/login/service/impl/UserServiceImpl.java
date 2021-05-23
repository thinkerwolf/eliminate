package com.thinkerwolf.eliminate.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.common.config.SessionManagerHolder;
import com.thinkerwolf.eliminate.login.dto.LoginUserDto;
import com.thinkerwolf.eliminate.login.entity.Player;
import com.thinkerwolf.eliminate.login.entity.User;
import com.thinkerwolf.eliminate.login.mapper.UserMapper;
import com.thinkerwolf.eliminate.login.service.IPlayerService;
import com.thinkerwolf.eliminate.login.service.IUserService;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.rpc.common.KfUtils;
import com.thinkerwolf.eliminate.rpc.common.ServerType;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerSessionVo;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerLoginDto;
import com.thinkerwolf.eliminate.rpc.login.service.ILoginToGatewayService;
import com.thinkerwolf.gamer.common.ServiceLoader;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.balance.LoadBalancer;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.registry.Registry;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Logger LOG = InternalLoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired(required = false)
    Registry registry;

    @Autowired
    IPlayerService playerService;

    @Override
    public OpResult login(Request request, LoginUserDto loginUserDto) {
        if (StringUtils.isBlank(loginUserDto.getUsername())) {
            return OpResult.fail(LocalMessages.T_USER_1);
        }
        if (registry == null) {
            return OpResult.fail(LocalMessages.T_KF_1);
        }
        List<URL> gameServers = KfUtils.lookup(registry, ServerType.GAME, request.getProtocol());
        if (gameServers.isEmpty()) {
            return OpResult.fail(LocalMessages.T_KF_2);
        }

        List<URL> gatewayServers = KfUtils.lookup(registry, ServerType.GATEWAY, request.getProtocol());
        if (gatewayServers.isEmpty()) {
            return OpResult.fail(LocalMessages.T_KF_3);
        }
        LoadBalancer balancer = ServiceLoader.getService("consistentHash", LoadBalancer.class);

        // Choose player
        User user = getUser(loginUserDto.getUsername());
        if (user == null) {
            user = new User();
            user.setUsername(loginUserDto.getUsername());
            user.setPassword(loginUserDto.getPassword());
            user.setPlatform(loginUserDto.getPlatform());
            user.setYx(loginUserDto.getYx());
            user.setPic(loginUserDto.getPic());
        }
        user.setLastLoginTime(LocalDateTime.now());
        saveOrUpdate(user);

        Player player;
        List<Player> players = playerService.getPlayerList(user.getUserId());
        if (players.isEmpty()) {
            player = new Player();
            player.setPlayerName(RandomStringUtils.randomAlphanumeric(6));
            player.setUserId(user.getUserId());
            player.setPic(loginUserDto.getPic());
            playerService.saveOrUpdate(player);
        } else {
            player = players.get(0);
        }

        if (StringUtils.isBlank(player.getGameServer())) {
            URL gatewayServer = balancer.select(gameServers, loginUserDto.getUsername(), Collections.emptyMap());
            player.setGameServer(gatewayServer.getString(URL.NODE_NAME));
            playerService.saveOrUpdate(player);
        }
        Session oldSession = Players.getSession(player.getPlayerId());
        if (oldSession != null) {
            Players.push(player.getPlayerId(), PushCommand.PLAYER_OFFLINE, LocalMessages.T_SESSION_2);
            oldSession.expire();
            Players.removeDto(oldSession);
        }

        Session session = SessionManagerHolder.get().getSession(null, true);
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(player.getPlayerId());
        playerDto.setPic(player.getPic());
        playerDto.setPlayerName(player.getPlayerName());
        playerDto.setUserId(user.getUserId());
        Players.addDto(session, playerDto);

        // Player gateway login
        PlayerSessionVo sessionData = new PlayerSessionVo();
        sessionData.setGameServer(player.getGameServer());
        sessionData.setPlayerId(player.getPlayerId());
        sessionData.setUserId(player.getUserId());
        sessionData.setSessionId(session.getId());
        sessionData.setProtocol(request.getProtocol());
        sessionData.setPlayerName(player.getPlayerName());
        sessionData.setPlayerPic(player.getPic());

        URL gatewayUrl = balancer.select(gatewayServers, user.getUserId() + "_" + player.getPlayerId(), null);
        try {
            ILoginToGatewayService loginToGatewayService = KfUtils.getRpcService(ILoginToGatewayService.class, gatewayUrl);
            PlayerLoginDto result = loginToGatewayService.loginPlayer(sessionData);
            if (!result.isSuc()) {
                return OpResult.fail(LocalMessages.T_KF_4);
            }
            Map<String, Object> data = Maps.newHashMapWithExpectedSize(2);

            // gateway信息
            Map<String, Object> gatewayData = Maps.newHashMapWithExpectedSize(3);
            gatewayData.put("host", gatewayUrl.getHost());
            gatewayData.put("port", gatewayUrl.getPort());
            gatewayData.put("serverType", ServerType.GATEWAY.getName());
            data.put("gateway", gatewayData);

            // player信息
            Map<String, Object> playerData = Maps.newHashMapWithExpectedSize(3);
            playerData.put("playerId", player.getPlayerId());
            playerData.put("playerName", player.getPlayerName());
            playerData.put("sessionId", session.getId());
            playerData.put("gameServer", player.getGameServer());
            data.put("player", playerData);

            return OpResult.ok(data);
        } catch (Exception e) {
            LOG.error("远程登录异常", e);
            return OpResult.fail(LocalMessages.T_KF_5);
        }
    }

    @Override
    public User getUser(String username) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        return getOne(qw);
    }
}
