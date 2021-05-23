package com.thinkerwolf.eliminate.rpc.chat.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class ChatDto implements Serializable {
    private String id;
    /**
     * 消息类型
     */
    private int type;
    /**
     * 消息日期
     */
    private Date date;
    /**
     * 发送消息的玩家id
     */
    private int playerId;
    /**
     * 玩家所在游戏服务器id
     */
    private String serverId;
    /**
     * 发送消息玩家图片
     */
    private String playerPic;
    /**
     * 消息文字
     */
    private String text;

    /**
     * 红包钱数
     */
    private double toMoney;
    /**
     * 红包剩余
     */
    private double curMoney;
    /**
     *
     */
    private int toNum;
    /**
     * 当前红包还剩多少
     */
    private int curNum;
}
