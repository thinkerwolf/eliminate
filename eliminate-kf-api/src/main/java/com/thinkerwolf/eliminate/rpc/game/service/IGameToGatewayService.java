package com.thinkerwolf.eliminate.rpc.game.service;

import com.thinkerwolf.gamer.rpc.annotation.RpcClient;

@RpcClient(serialize = "hessian2")
public interface IGameToGatewayService {

    void login(String sessionId);

}
