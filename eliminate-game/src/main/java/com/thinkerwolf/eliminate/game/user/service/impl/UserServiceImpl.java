package com.thinkerwolf.eliminate.game.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.common.*;
import com.thinkerwolf.eliminate.game.player.entity.Player;
import com.thinkerwolf.eliminate.game.player.service.IPlayerService;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.user.UserDto;
import com.thinkerwolf.eliminate.pub.user.Users;
import com.thinkerwolf.eliminate.game.user.entity.User;
import com.thinkerwolf.eliminate.game.user.mapper.UserMapper;
import com.thinkerwolf.eliminate.game.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Session;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-05-17
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    IPlayerService playerService;

    @Override
    public OpResult login(Request request, String username, String password) {
        if (StringUtils.isBlank(username)) {
            return OpResult.fail(LocalMessages.T_USER_1);
        }
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", username);
        User user = getOne(userWrapper);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPic("1");
        }
        user.setLastLoginTime(LocalDateTime.now());
        saveOrUpdate(user);
        Session session = request.getSession(true);
        Session oldSession = Users.getSession(username);
        if (oldSession != null) {
            // 通知被顶替
            Players.push(session, PushCommand.PLAYER_OFFLINE, LocalMessages.T_SESSION_2);
            oldSession.expire();
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setPic(user.getPic());

        session.setAttribute(user.getUserId(), userDto);

        QueryWrapper<Player> playerWrapper = new QueryWrapper<>();
        playerWrapper.eq("user_id", user.getUserId());
        List<Player> playerList = playerService.list(playerWrapper);
        if (playerList.size() == 0) {
            Player player = new Player();
            Integer maxId = playerService.getMaxId();
            player.setPlayerId(maxId == null ? 1 : maxId + 1);
            player.setUserId(user.getUserId());
            player.setPlayerName("");
            player.setPic("1");
            player.setVit(SDataBusManager.getDataGetter().getMiscCache().getInt(MiscKey.VIT_MAX));
            playerService.saveOrUpdate(player);
            playerList = playerService.list(playerWrapper);
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", user.getUserId());

        List<PlayerDto> playerDtos = Lists.newArrayList();
        for (Player player : playerList) {
            PlayerDto playerDto = new PlayerDto();
            playerDto.setPlayerId(player.getPlayerId());
            playerDto.setPlayerName(player.getPlayerName());
            playerDto.setUserId(player.getUserId());
            playerDto.setPic(player.getPic());
            playerDtos.add(playerDto);
        }
        map.put("playerList", playerDtos);
        return OpResult.ok(map);
    }
}
