package com.thinkerwolf.eliminate.rpc.login.service;

import com.thinkerwolf.eliminate.rpc.login.entity.PlayerSessionVo;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerLoginDto;
import com.thinkerwolf.gamer.rpc.annotation.RpcMethod;

/**
 * Login服到Gateway服
 *
 * @author wukai
 */
public interface ILoginToGatewayService {

    @RpcMethod
    PlayerLoginDto loginPlayer(PlayerSessionVo data);
}
