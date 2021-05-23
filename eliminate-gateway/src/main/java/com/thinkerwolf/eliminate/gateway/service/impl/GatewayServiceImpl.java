package com.thinkerwolf.eliminate.gateway.service.impl;

import com.thinkerwolf.eliminate.gateway.databus.DataBusManager;
import com.thinkerwolf.eliminate.gateway.service.IGatewayService;
import com.thinkerwolf.eliminate.rpc.common.KfUtils;
import com.thinkerwolf.eliminate.rpc.common.ServerType;
import com.thinkerwolf.eliminate.rpc.gateway.service.IGatewayToGameClient;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.registry.Registry;
import com.thinkerwolf.gamer.remoting.Protocol;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("gatewayService")
public class GatewayServiceImpl implements IGatewayService {

    @Override
    public List<Integer> toGameTest(String serverType, String serverId, int num) {
        Registry registry = DataBusManager.getDataBus().getRegistry();
        ServerType st = ServerType.nameOf(serverType);
        List<URL> remoteUrls = KfUtils.lookup(registry, st, Protocol.WEBSOCKET, serverId);
        IGatewayToGameClient service = KfUtils.getRpcService(IGatewayToGameClient.class, remoteUrls.get(0));
        return service.test(num);
    }
}
