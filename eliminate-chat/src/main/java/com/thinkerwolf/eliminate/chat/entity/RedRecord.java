package com.thinkerwolf.eliminate.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

/**
 * 红包数据
 *
 * @author wukai
 */
@Data
@RedisHash(value = "red_record")
public class RedRecord implements Serializable {

    @Id
    private String id;
    private int playerId;
    private String playerPic;
    private String game;
    private String serverId;
    @Indexed
    private String redId;
    private Date date;

}
