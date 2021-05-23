package com.thinkerwolf.eliminate.rpc.login.service;

import com.thinkerwolf.eliminate.rpc.login.entity.PlayerSessionVo;
import com.thinkerwolf.eliminate.rpc.login.entity.PlayerLoginDto;
import com.thinkerwolf.gamer.rpc.annotation.RpcClient;

/**
 * Login服到Gateway服
 *
 * @author wukai
 */
@RpcClient
public interface ILoginToGatewayService {

    PlayerLoginDto loginPlayer(PlayerSessionVo data);

}
