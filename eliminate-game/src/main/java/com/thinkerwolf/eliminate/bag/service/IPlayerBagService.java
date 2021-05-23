package com.thinkerwolf.eliminate.bag.service;

import com.thinkerwolf.eliminate.bag.entity.PlayerBag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thinkerwolf.eliminate.common.OpResult;

/**
 * <p>
 * 服务类，玩家背包
 * </p>
 *
 * @author wukai
 * @since 2020-06-10
 */
public interface IPlayerBagService extends IService<PlayerBag> {

    PlayerBag getBag(int playerId, int type, int detailId);

    PlayerBag getBagSafty(int playerId, int type, int detailId);


}
