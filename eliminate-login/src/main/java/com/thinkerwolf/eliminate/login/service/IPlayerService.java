package com.thinkerwolf.eliminate.login.service;

import com.thinkerwolf.eliminate.login.entity.Player;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wukai
 * @since 2020-06-25
 */
public interface IPlayerService extends IService<Player> {
    /**
     * 获取玩家列表
     *
     * @param userId
     * @return
     */
    List<Player> getPlayerList(int userId);

}
