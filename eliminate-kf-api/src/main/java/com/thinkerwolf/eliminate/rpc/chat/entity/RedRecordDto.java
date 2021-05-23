package com.thinkerwolf.eliminate.rpc.chat.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class RedRecordDto implements Serializable {
    private String id;
    private int playerId;
    private String playerPic;
    private String redId;
    private Date date;
}
