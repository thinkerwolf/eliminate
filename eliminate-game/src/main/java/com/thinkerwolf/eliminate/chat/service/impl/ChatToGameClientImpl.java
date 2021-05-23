package com.thinkerwolf.eliminate.chat.service.impl;

import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.rpc.chat.entity.ChatDto;
import com.thinkerwolf.eliminate.rpc.chat.service.IChatToGameClient;

public class ChatToGameClientImpl implements IChatToGameClient {
    @Override
    public void notifyChat(ChatDto chatDto) {
        Players.push(PushCommand.CHAT_RECEIVE, chatDto);
    }

}
