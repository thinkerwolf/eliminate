package com.thinkerwolf.eliminate.game.chat.service;

import com.thinkerwolf.eliminate.common.OpResult;

/**
 * 聊天相关Service
 *
 * @author wukai
 * @since 2020-09-26
 */
public interface IChatService {
    /**
     * 发送文字消息
     *
     * @param playerId 玩家id
     * @param text     文字内容
     * @return
     */
    OpResult sendText(int playerId, String text);

    /**
     * 发送文字消息
     *
     * @param playerId 玩家id
     * @param money    红包大小
     * @param num      红包个数
     * @return
     */
    OpResult sendRed(int playerId, double money, int num);

    /**
     * 获取聊天历史
     *
     * @param playerId 玩家id
     * @return
     */
    OpResult getChats(int playerId, int page);

    /**
     * 领取红包
     *
     * @param playerId 玩家id
     * @param redId    红包id
     * @return
     */
    OpResult takeRed(int playerId, String redId);
}
