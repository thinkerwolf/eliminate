package com.thinkerwolf.eliminate.gateway;

import com.thinkerwolf.eliminate.common.config.SessionManagerHolder;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.player.entity.Player;
import com.thinkerwolf.eliminate.player.service.IPlayerService;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfo;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfoManager;
import com.thinkerwolf.eliminate.rpc.gateway.service.IGatewayToGameClient;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerSessionVo;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerLoginDto;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.servlet.Session;

import java.util.ArrayList;
import java.util.List;

public class GatewayToGameClientImpl implements IGatewayToGameClient {

    private static final Logger LOG = InternalLoggerFactory.getLogger(GatewayToGameClientImpl.class);

    @Override
    public List<Integer> test(int num) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(num);
        }
        return list;
    }

    @Override
    public PlayerLoginDto loginPlayer(PlayerSessionVo data) {
        IPlayerService playerService = DataBusManager.getDataBus().getPlayerService();
        Player player = playerService.getById(data.getPlayerId());
        if (player == null) {
            player = new Player();
            player.setPlayerId(data.getPlayerId());
            player.setPic(data.getPlayerPic());
            player.setPlayerName(data.getPlayerName());
            player.setUserId(data.getUserId());
            player.setVit(10);
            playerService.saveOrUpdate(player);
        }

        PlayerRoleInfo roleInfo = new PlayerRoleInfo();
        roleInfo.setPlayerId(player.getPlayerId());
        roleInfo.setPlayerName(player.getPlayerName());
        roleInfo.setVit(player.getVit());
        roleInfo.setPic(player.getPic());
        PlayerRoleInfoManager.getInstance().setRole(roleInfo);

        // session 处理
        Session session = SessionManagerHolder.get().getSession(data.getSessionId(), true);
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(player.getPlayerId());
        playerDto.setUserId(player.getUserId());
        playerDto.setPic(player.getPic());
        playerDto.setPlayerName(player.getPlayerName());
        session.setAttribute(player.getPlayerId(), playerDto);

        PlayerLoginDto result = new PlayerLoginDto(true);
        result.setPlayerId(player.getPlayerId());
        result.setPlayerName(player.getPlayerName());
        result.setPlayerPic(player.getPic());
        return result;
    }
}
