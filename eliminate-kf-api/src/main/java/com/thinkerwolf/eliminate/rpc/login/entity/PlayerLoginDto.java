package com.thinkerwolf.eliminate.rpc.login.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlayerLoginDto implements Serializable {

    private boolean suc;
    private int playerId;
    private String playerName;
    private String playerPic;

    public PlayerLoginDto(boolean suc) {
        this.suc = suc;
    }
}
