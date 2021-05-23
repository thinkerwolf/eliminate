package com.thinkerwolf.eliminate.rpc.chat.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天文字数据
 *
 * @author wukai
 */
@Data
@Builder
public class ChatTextVo implements Serializable {

    private int playerId;
    private String playerPic;
    private String game;
    private String serverId;
    private Date date;
    private String text;

}
