package com.thinkerwolf.eliminate.rpc.chat.service;

import com.thinkerwolf.eliminate.rpc.chat.entity.ChatDto;
import com.thinkerwolf.gamer.rpc.annotation.RpcMethod;

public interface IChatToGameClient {

    @RpcMethod
    void notifyChat(ChatDto chatDto);

}
