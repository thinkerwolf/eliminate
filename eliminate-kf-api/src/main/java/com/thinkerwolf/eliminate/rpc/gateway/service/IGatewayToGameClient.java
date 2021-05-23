package com.thinkerwolf.eliminate.rpc.gateway.service;

import com.thinkerwolf.eliminate.rpc.login.entity.PlayerSessionVo;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerLoginDto;
import com.thinkerwolf.gamer.rpc.annotation.RpcClient;

import java.util.List;

/**
 * Gateway服到Game服
 *
 * @author wukai
 */
@RpcClient(serialize = "hessian2")
public interface IGatewayToGameClient {

    List<Integer> test(int num);

    PlayerLoginDto loginPlayer(PlayerSessionVo data);

}
