package com.thinkerwolf.eliminate.rpc.login.entity;

import com.thinkerwolf.gamer.remoting.Protocol;
import lombok.Data;

import java.io.Serializable;

/**
 * 登陆服玩家数据
 *
 * @author wukai
 */
@Data
public class PlayerSessionVo implements Serializable {

    private int userId;
    private int playerId;
    private String playerPic;
    private String playerName;
    private String sessionId;
    private Protocol protocol;
    private String gameServer;
}
