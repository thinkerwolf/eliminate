package com.thinkerwolf.eliminate.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;

/**
 * 聊天数据
 *
 * @author wukai
 */
@Data
@RedisHash(value = "chat")
public class Chat {
    @Id
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
    @Indexed
    private int playerId;
    /**
     * 玩家所在游戏名称
     */
    private String game;
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

    @TimeToLive
    private Long expiration;
}
