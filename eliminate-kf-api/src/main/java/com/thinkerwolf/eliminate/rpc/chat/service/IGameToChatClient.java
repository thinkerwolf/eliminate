package com.thinkerwolf.eliminate.rpc.chat.service;

import com.thinkerwolf.eliminate.rpc.chat.entity.*;
import com.thinkerwolf.gamer.rpc.annotation.RpcClient;

import java.util.List;

/**
 * 游戏服到聊天服
 *
 * @author wukai
 * @since 2020-09-01
 */
@RpcClient
public interface IGameToChatClient {
    /**
     * 向全服发送文字消息
     *
     * @param chatTextVo 文字消息vo
     * @return 发送结果
     */
    ChatSendDto sendText(ChatTextVo chatTextVo);

    /**
     * 向全服发送红包消息
     *
     * @param chatRedVo 红包消息vo
     * @return 发送结果
     */
    ChatSendDto sendRed(ChatRedVo chatRedVo);

    /**
     * 领取红包
     *
     * @param redTakeVo 红包领取vo
     * @return 领取结果
     */
    ChatSendDto takeRed(RedTakeVo redTakeVo);

    /**
     * 获取全服聊天记录
     *
     * @param p    page
     * @param size size
     * @return 聊天列表
     */
    List<ChatDto> getChats(int p, int size);

    /**
     * 获取红包领取记录
     *
     * @param redId 红包id
     * @return
     */
    List<RedRecordDto> getRedRecords(String redId);

}
