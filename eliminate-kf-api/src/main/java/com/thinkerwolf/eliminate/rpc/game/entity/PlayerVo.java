package com.thinkerwolf.eliminate.rpc.game.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlayerVo implements Serializable {

    private int playerId;
    private String playerName;
}
