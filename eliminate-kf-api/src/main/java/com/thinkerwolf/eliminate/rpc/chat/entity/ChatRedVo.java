package com.thinkerwolf.eliminate.rpc.chat.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 聊天红包数据
 *
 * @author wukai
 */
@Data
@Builder
public class ChatRedVo implements Serializable {
    private int playerId;
    private String playerPic;
    private String game;
    private String serverId;
    private Date date;
    private String text;
    private double money;
    private int num;
}
