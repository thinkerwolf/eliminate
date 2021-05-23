package com.thinkerwolf.eliminate.game.diary.service;

import com.thinkerwolf.eliminate.game.diary.entity.PlayerDay;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wukai
 * @since 2020-08-05
 */
public interface IPlayerDayService extends IService<PlayerDay> {

    PlayerDay find(int playerId, int day);

    PlayerDay findOrCreate(int playerId, int day);

}
