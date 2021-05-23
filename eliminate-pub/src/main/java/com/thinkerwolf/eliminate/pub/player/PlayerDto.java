package com.thinkerwolf.eliminate.pub.player;

import lombok.Data;

@Data
public class PlayerDto {

    private Integer playerId;

    private Integer userId;

    private String playerName;
    /**
     * 玩家pic
     */
    private String pic;
}
