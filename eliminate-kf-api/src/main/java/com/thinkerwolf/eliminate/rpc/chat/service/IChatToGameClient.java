package com.thinkerwolf.eliminate.rpc.chat.service;

import com.thinkerwolf.eliminate.rpc.chat.entity.ChatDto;
import com.thinkerwolf.gamer.rpc.annotation.RpcClient;

@RpcClient
public interface IChatToGameClient {

    void notifyChat(ChatDto chatDto);

}
