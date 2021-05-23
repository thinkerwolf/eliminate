package com.thinkerwolf.eliminate.chat.game;

import com.google.common.collect.Lists;
import com.thinkerwolf.eliminate.game.chat.entity.Chat;
import com.thinkerwolf.eliminate.game.chat.entity.RedRecord;
import com.thinkerwolf.eliminate.game.chat.repository.ChatRepository;
import com.thinkerwolf.eliminate.game.chat.repository.RedRecordRepository;
import com.thinkerwolf.eliminate.common.EliminateConstants;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.rpc.chat.comm.ChatConstants;
import com.thinkerwolf.eliminate.rpc.chat.comm.ChatType;
import com.thinkerwolf.eliminate.rpc.chat.entity.*;
import com.thinkerwolf.eliminate.rpc.chat.service.IChatToGameClient;
import com.thinkerwolf.eliminate.rpc.common.KfUtils;
import com.thinkerwolf.eliminate.rpc.common.ServerType;
import com.thinkerwolf.eliminate.rpc.chat.service.IGameToChatClient;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.registry.Registry;
import com.thinkerwolf.gamer.remoting.Protocol;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GameToChatClientImpl implements IGameToChatClient {

    private static final Logger LOG = InternalLoggerFactory.getLogger(GameToChatClientImpl.class);

    @Autowired
    Registry registry;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    RedRecordRepository redRecordRepository;

    @Override
    public ChatSendDto sendText(ChatTextVo chatTextVo) {
        Chat chat = new Chat();
        chat.setType(ChatType.TEXT.getId());
        chat.setDate(chatTextVo.getDate());
        chat.setPlayerId(chatTextVo.getPlayerId());
        chat.setPlayerPic(chatTextVo.getPlayerPic());
        chat.setText(chatTextVo.getText());
        chat.setGame(chatTextVo.getGame());
        chat.setServerId(chatTextVo.getServerId());
        chat.setExpiration(ChatConstants.CHAT_TEXT_EXPIRATION);

        try {
            Chat resultChat = chatRepository.save(chat);
            ChatDto chatDto = ChatDto.builder()
                    .id(chat.getId())
                    .type(resultChat.getType())
                    .date(resultChat.getDate())
                    .playerId(resultChat.getPlayerId())
                    .playerPic(resultChat.getPlayerPic())
                    .serverId(resultChat.getServerId())
                    .text(resultChat.getText())
                    .build();

            // 其他game服务器发送
            if (registry != null) {
                List<URL> gameUrls = KfUtils.lookup(registry, ServerType.GAME, Protocol.WEBSOCKET);
                for (URL url : gameUrls) {
                    final URL gameUrl = url;
                    EliminateConstants.COMMON_SCHEDULED.submit(() -> {
                        try {
                            IChatToGameClient chatToGameService = KfUtils.getRpcService(IChatToGameClient.class, gameUrl);
                            chatToGameService.notifyChat(chatDto);
                        } catch (Exception e) {
                            LOG.error("Notify url " + gameUrl, e);
                        }
                    });
                }
            }
            return ChatSendDto.builder().suc(true).result(resultChat.getId()).build();
        } catch (Exception e) {
            LOG.error("Redis chat error", e);
            return ChatSendDto.builder().suc(false).errMsg(LocalMessages.T_CHAT_9).build();
        }

    }

    @Override
    public ChatSendDto sendRed(final ChatRedVo chatRedVo) {
        Chat chat = new Chat();
        chat.setType(ChatType.RED.getId());
        chat.setDate(chatRedVo.getDate());
        chat.setPlayerId(chatRedVo.getPlayerId());
        chat.setPlayerPic(chatRedVo.getPlayerPic());
        chat.setText(chatRedVo.getText());
        chat.setGame(chatRedVo.getGame());
        chat.setServerId(chatRedVo.getServerId());
        chat.setCurMoney(chatRedVo.getMoney());
        chat.setCurNum(chatRedVo.getNum());
        chat.setToMoney(chatRedVo.getMoney());
        chat.setToNum(chatRedVo.getNum());
        chat.setExpiration(ChatConstants.CHAT_RED_EXPIRATION);
        try {
            Chat resultChat = chatRepository.save(chat);
            ChatDto chatDto = ChatDto.builder()
                    .id(chat.getId())
                    .type(resultChat.getType())
                    .date(resultChat.getDate())
                    .playerId(resultChat.getPlayerId())
                    .playerPic(resultChat.getPlayerPic())
                    .serverId(resultChat.getServerId())
                    .text(resultChat.getText())
                    .toMoney(resultChat.getToMoney())
                    .toNum(resultChat.getToNum())
                    .curMoney(resultChat.getCurMoney())
                    .curNum(resultChat.getCurNum())
                    .build();

            // 其他game服务器发送
            if (registry != null) {
                List<URL> gameUrls = KfUtils.lookup(registry, ServerType.GAME, Protocol.WEBSOCKET);
                for (URL url : gameUrls) {
                    final URL gameUrl = url;
                    EliminateConstants.COMMON_SCHEDULED.submit(() -> {
                        try {
                            IChatToGameClient chatToGameService = KfUtils.getRpcService(IChatToGameClient.class, gameUrl);
                            chatToGameService.notifyChat(chatDto);
                        } catch (Exception e) {
                            LOG.error("Notify url " + gameUrl, e);
                        }
                    });
                }
            }
            return ChatSendDto.builder().suc(true).result(resultChat.getId()).build();
        } catch (Exception e) {
            LOG.error("Redis chat error", e);
            return ChatSendDto.builder().suc(false).errMsg(LocalMessages.T_CHAT_9).build();
        }
    }

    @Override
    public ChatSendDto takeRed(RedTakeVo redTakeVo) {
        Optional<Chat> op = chatRepository.findById(redTakeVo.getRedId());
        if (!op.isPresent()) {
            return ChatSendDto.builder().suc(false).errMsg(LocalMessages.T_CHAT_5).build();
        }
        Chat chat = op.get();
        if (chat.getType() != ChatType.RED.getId()) {
            return ChatSendDto.builder().suc(false).errMsg(LocalMessages.T_CHAT_6).build();
        }
        if (chat.getCurNum() == 0) {
            return ChatSendDto.builder().suc(false).errMsg(LocalMessages.T_CHAT_7).build();
        }

        // 插入一条领取记录
        String id = redTakeVo.getPlayerId() +
                "_" +
                redTakeVo.getGame() +
                "_" +
                redTakeVo.getServerId();
        if (redRecordRepository.findById(id).isPresent()) {
            return ChatSendDto.builder().suc(false).errMsg(LocalMessages.T_CHAT_8).build();
        }

        double money = getRandomMoney(chat);
        RedRecord redRecord = new RedRecord();
        redRecord.setId(id);
        redRecord.setRedId(redTakeVo.getRedId());
        redRecord.setDate(new Date());
        redRecord.setPlayerId(redTakeVo.getPlayerId());
        redRecord.setPlayerPic(redTakeVo.getPlayerPic());
        redRecord.setGame(redTakeVo.getGame());
        redRecord.setServerId(redTakeVo.getServerId());
        redRecordRepository.save(redRecord);
        chatRepository.save(chat);
        return ChatSendDto.builder().suc(true).result(money).build();
    }

    @Override
    public List<ChatDto> getChats(int p, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(p, size, sort);
        Page<Chat> page = chatRepository.findAll(pageable);
        List<Chat> chats = page.getContent();
        List<ChatDto> chatDs = Lists.newArrayList();
        for (Chat chat : chats) {
            ChatDto chatDto = ChatDto.builder()
                    .id(chat.getId())
                    .type(chat.getType())
                    .date(chat.getDate())
                    .playerId(chat.getPlayerId())
                    .playerPic(chat.getPlayerPic())
                    .serverId(chat.getServerId())
                    .text(chat.getText())
                    .toMoney(chat.getToMoney())
                    .toNum(chat.getToNum())
                    .curMoney(chat.getCurMoney())
                    .curNum(chat.getCurNum())
                    .build();
            chatDs.add(chatDto);
        }
        return chatDs;
    }

    @Override
    public List<RedRecordDto> getRedRecords(String redId) {
        List<RedRecord> redRecords = redRecordRepository.findByRedId(redId);
        List<RedRecordDto> result = Lists.newArrayList();
        for (RedRecord redRecord : redRecords) {
            RedRecordDto dto = RedRecordDto.builder()
                    .id(redRecord.getId())
                    .date(redRecord.getDate())
                    .playerId(redRecord.getPlayerId())
                    .playerPic(redRecord.getPlayerPic())
                    .redId(redId)
                    .build();
            result.add(dto);
        }
        result.sort(redRecordDtoComparator);
        return result;
    }

    private static final Comparator<RedRecordDto> redRecordDtoComparator = (o1, o2) -> {
        long t1 = o1.getDate().getTime();
        long t2 = o2.getDate().getTime();
        if (t1 > t2) {
            return 1;
        } else if (t1 < t2) {
            return -1;
        }
        return 0;
    };

    public static double getRandomMoney(Chat chat) {
        final int curNum = chat.getCurNum();
        final double curMoney = chat.getCurMoney();
        if (curNum == 0) {
            return 0.0;
        }
        if (curNum == 1) {
            chat.setCurNum(0);
            chat.setCurMoney(0);
            return (double) Math.round(curMoney * 100) / 100;
        }
        double min = 0.01;
        double max = curMoney / curNum * 2;
        double money = RandomUtils.nextDouble() * max;
        money = money <= min ? 0.01D : money;
        money = Math.floor(money * 100) / 100;
        chat.setCurNum(curNum - 1);
        chat.setCurMoney(curMoney - money);
        return money;
    }
}
