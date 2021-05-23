package com.thinkerwolf.eliminate.game.player.service;

import com.thinkerwolf.eliminate.game.player.entity.Player;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.core.servlet.Request;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wukai
 * @since 2020-05-17
 */
public interface IPlayerService extends IService<Player> {
    /**
     * 玩家登录
     *
     * @param request
     * @param playerId
     * @return
     */
    OpResult loginPlayer(Request request, int playerId);

    /**
     * 网关登录
     *
     * @param request
     * @param playerId
     * @return
     */
    OpResult gwLogin(Request request, int playerId);

    Integer getMaxId();

    /**
     * 玩家登陆后做的一些事情
     *
     * @param playerId
     */
    void doAfterPlayerLogin(int playerId);

    /**
     * 体力恢复规划
     *
     * @param playerId
     */
    void vitRecover(int playerId);

    void pushStar(int playerId);
}
