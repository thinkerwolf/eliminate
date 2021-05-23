package com.thinkerwolf.eliminate.rpc.chat.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
public class RedTakeVo implements Serializable {

    private int playerId;
    private String playerPic;
    private String game;
    private String serverId;
    private String redId;

}
