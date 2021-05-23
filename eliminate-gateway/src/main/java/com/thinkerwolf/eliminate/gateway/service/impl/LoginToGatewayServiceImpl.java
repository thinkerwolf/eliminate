package com.thinkerwolf.eliminate.gateway.service.impl;

import com.thinkerwolf.eliminate.common.config.SessionManagerHolder;
import com.thinkerwolf.eliminate.gateway.databus.DataBusManager;
import com.thinkerwolf.eliminate.gateway.net.GatewaySessions;
import com.thinkerwolf.eliminate.rpc.common.KfUtils;
import com.thinkerwolf.eliminate.rpc.common.ServerType;
import com.thinkerwolf.eliminate.rpc.gateway.service.IGatewayToGameClient;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerSessionVo;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerLoginDto;
import com.thinkerwolf.eliminate.rpc.login.service.ILoginToGatewayService;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.registry.Registry;

import java.util.List;

/**
 * 登陆服到gateway
 *
 * @author wukai
 */
public class LoginToGatewayServiceImpl implements ILoginToGatewayService {

    private static final Logger LOG = InternalLoggerFactory.getLogger(LoginToGatewayServiceImpl.class);

    @Override
    public PlayerLoginDto loginPlayer(PlayerSessionVo data) {
        Registry registry = DataBusManager.getDataBus().getRegistry();
        if (registry == null) {
            return new PlayerLoginDto(false);
        }
        List<URL> urls = KfUtils.lookup(registry, ServerType.GAME, data.getProtocol(), data.getGameServer());
        if (urls.isEmpty()) {
            return new PlayerLoginDto(false);
        }
        IGatewayToGameClient gatewayToGameService = KfUtils.getRpcService(IGatewayToGameClient.class, urls.get(0));
        PlayerLoginDto result = gatewayToGameService.loginPlayer(data);
        if (result.isSuc()) {
            Session session = SessionManagerHolder.get().getSession(data.getSessionId(), true);
            if (!session.getId().equals(data.getSessionId())) {
                result.setSuc(false);
                LOG.error("Gateway session not equal " + session.getId() + " != " + data.getSessionId());
                return result;
            }
            session.touch();
            GatewaySessions.getInstance().putSession(session);
        }
        return result;
    }


}
