package com.thinkerwolf.eliminate.chat.service.impl;

import com.thinkerwolf.eliminate.chat.service.IChatService;
import com.thinkerwolf.eliminate.common.EliminateConstants;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfo;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfoManager;
import com.thinkerwolf.eliminate.rpc.chat.entity.*;
import com.thinkerwolf.eliminate.rpc.common.KfUtils;
import com.thinkerwolf.eliminate.rpc.common.ServerType;
import com.thinkerwolf.eliminate.rpc.chat.service.IGameToChatClient;
import com.thinkerwolf.gamer.common.ServiceLoader;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.balance.LoadBalancer;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.registry.Registry;
import com.thinkerwolf.gamer.remoting.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("chatService")
public class ChatServiceImpl implements IChatService {
    private static final Logger LOG = InternalLoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired(required = false)
    Registry registry;

    @Override
    public OpResult sendText(int playerId, String text) {
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(playerId);
        if (roleInfo == null) {
            return OpResult.fail(LocalMessages.T_PLAYER_1);
        }
        URL url = selectChatServer(roleInfo);
        if (url == null) {
            return OpResult.fail(LocalMessages.T_CHAT_2);
        }

        ChatTextVo textVo = ChatTextVo.builder()
                .date(new Date())
                .text(text)
                .playerId(playerId)
                .playerPic(roleInfo.getPic())
                .serverId(EliminateConstants.GAME_ID)
                .game(EliminateConstants.GAME_NAME)
                .build();

        try {
            IGameToChatClient gt = KfUtils.getRpcService(IGameToChatClient.class, url);
            ChatSendDto sendDto = gt.sendText(textVo);
            return sendDto.isSuc() ? OpResult.ok(sendDto.getResult()) : OpResult.fail(sendDto.getErrMsg());
        } catch (Exception e) {
            LOG.error("Chat error", e);
        }
        return OpResult.fail(LocalMessages.T_CHAT_1);
    }

    @Override
    public OpResult sendRed(int playerId, double money, int num) {
        if (money <= 0.0) {
            return OpResult.fail(LocalMessages.T_CHAT_3);
        }
        if (num <= 0) {
            return OpResult.fail(LocalMessages.T_CHAT_4);
        }
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(playerId);
        if (roleInfo == null) {
            return OpResult.fail(LocalMessages.T_PLAYER_1);
        }
        URL url = selectChatServer(roleInfo);
        if (url == null) {
            return OpResult.fail(LocalMessages.T_CHAT_2);
        }
        ChatRedVo redVo = ChatRedVo.builder()
                .date(new Date())
                .text("")
                .playerId(playerId)
                .playerPic(roleInfo.getPic())
                .serverId(EliminateConstants.GAME_ID)
                .game(EliminateConstants.GAME_NAME)
                .money(money)
                .num(num)
                .build();

        try {
            IGameToChatClient gt = KfUtils.getRpcService(IGameToChatClient.class, url);
            ChatSendDto sendDto = gt.sendRed(redVo);
            return sendDto.isSuc() ? OpResult.ok(sendDto.getResult()) : OpResult.fail(sendDto.getErrMsg());
        } catch (Exception e) {
            LOG.error("Chat error", e);
        }
        return OpResult.fail(LocalMessages.T_CHAT_1);
    }

    @Override
    public OpResult getChats(int playerId, int page) {
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(playerId);
        if (roleInfo == null) {
            return OpResult.fail(LocalMessages.T_PLAYER_1);
        }
        URL url = selectChatServer(roleInfo);
        if (url == null) {
            return OpResult.fail(LocalMessages.T_CHAT_2);
        }
        try {
            IGameToChatClient gt = KfUtils.getRpcService(IGameToChatClient.class, url);
            List<ChatDto> ds = gt.getChats(page, 20);
            return OpResult.ok(ds);
        } catch (Exception e) {
            LOG.error("Chat error", e);
        }
        return OpResult.fail(LocalMessages.T_CHAT_1);
    }

    @Override
    public OpResult takeRed(int playerId, String redId) {
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(playerId);
        if (roleInfo == null) {
            return OpResult.fail(LocalMessages.T_PLAYER_1);
        }
        URL url = selectChatServer(roleInfo);
        if (url == null) {
            return OpResult.fail(LocalMessages.T_CHAT_2);
        }
        RedTakeVo redTakeVo = RedTakeVo.builder()
                .redId(redId)
                .playerId(playerId)
                .playerPic(roleInfo.getPic())
                .game(EliminateConstants.GAME_NAME)
                .serverId(EliminateConstants.GAME_ID)
                .build();
        try {
            IGameToChatClient gt = KfUtils.getRpcService(IGameToChatClient.class, url);
            ChatSendDto sendDto = gt.takeRed(redTakeVo);
            return sendDto.isSuc() ? OpResult.ok(sendDto.getResult()) : OpResult.fail(sendDto.getErrMsg());
        } catch (Exception e) {
            LOG.error("Chat error", e);
        }
        return OpResult.fail(LocalMessages.T_CHAT_1);
    }

    private URL selectChatServer(PlayerRoleInfo roleInfo) {
        if (roleInfo.getChatServer() == null) {
            List<URL> urls = KfUtils.lookup(registry, ServerType.CHAT, Protocol.WEBSOCKET);
            if (urls.isEmpty()) {
                return null;
            }
            LoadBalancer balancer = ServiceLoader.getAdaptiveService(LoadBalancer.class);
            URL url = balancer.select(urls, String.valueOf(roleInfo.getPlayerId()), null);
            roleInfo.setChatServer(url);
            return url;
        }
        return roleInfo.getChatServer();
    }
}
