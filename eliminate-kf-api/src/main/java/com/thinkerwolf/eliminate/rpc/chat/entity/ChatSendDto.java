package com.thinkerwolf.eliminate.rpc.chat.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
public class ChatSendDto implements Serializable {

    private boolean suc;
    private Object result;
    private String errMsg;

}
