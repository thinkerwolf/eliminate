package com.thinkerwolf.eliminate.rpc.game.service;

import com.thinkerwolf.gamer.rpc.annotation.RpcMethod;

public interface IGameToGatewayService {

    @RpcMethod
    void login(String sessionId);
}
