package com.thinkerwolf.eliminate.building.service;

import com.thinkerwolf.eliminate.building.comm.PlayerBuildingManager;
import com.thinkerwolf.eliminate.building.entity.PlayerBuilding;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thinkerwolf.eliminate.common.OpResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wukai
 * @since 2020-05-24
 */
public interface IPlayerBuildingService extends IService<PlayerBuilding> {

    PlayerBuilding getBuilding(int playerId, int buildingId);

    /**
     * 升级建筑
     *
     * @param playerId
     * @param buildingId
     * @return
     */
    OpResult upgrade(int playerId, int buildingId);

    /**
     * 确认按钮
     *
     * @param playerId
     * @param buildingId
     * @return
     */
    OpResult confirm(int playerId, int buildingId);

    /**
     * 登录之后操作
     *
     * @param playerId
     */
    void handleLogin(int playerId);

    PlayerBuildingManager getManager(int buildingId);


}
