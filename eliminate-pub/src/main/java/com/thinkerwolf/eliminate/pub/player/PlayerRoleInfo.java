package com.thinkerwolf.eliminate.pub.player;

import com.thinkerwolf.gamer.common.URL;
import lombok.Data;

@Data
public class PlayerRoleInfo {

    private int playerId;
    private String playerName;
    private String pic;
    private volatile int vit;
    private String serverId;
    private URL chatServer;
}
