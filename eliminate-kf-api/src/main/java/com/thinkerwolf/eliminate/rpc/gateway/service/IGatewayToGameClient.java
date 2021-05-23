package com.thinkerwolf.eliminate.rpc.gateway.service;

import com.thinkerwolf.eliminate.rpc.login.entity.PlayerSessionVo;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerLoginDto;
import com.thinkerwolf.gamer.rpc.annotation.RpcMethod;

import java.util.List;

/**
 * Gateway服到Game服
 *
 * @author wukai
 */
public interface IGatewayToGameClient {

    @RpcMethod
    List<Integer> test(int num);

    @RpcMethod
    PlayerLoginDto loginPlayer(PlayerSessionVo data);

}
