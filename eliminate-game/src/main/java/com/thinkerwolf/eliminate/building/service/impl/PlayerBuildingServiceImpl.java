package com.thinkerwolf.eliminate.building.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.building.comm.PlayerBuildingManager;
import com.thinkerwolf.eliminate.building.entity.PlayerBuilding;
import com.thinkerwolf.eliminate.building.mapper.PlayerBuildingMapper;
import com.thinkerwolf.eliminate.building.service.IPlayerBuildingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.sdata.cache.BuildingCache;
import com.thinkerwolf.eliminate.pub.sdata.cache.BuildingLvCache;
import com.thinkerwolf.eliminate.pub.sdata.entity.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-05-24
 */
@Service("playerBuildingService")
public class PlayerBuildingServiceImpl extends ServiceImpl<PlayerBuildingMapper, PlayerBuilding> implements IPlayerBuildingService {

    @Autowired
    BuildingCache buildingCache;

    @Autowired
    BuildingLvCache buildingLvCache;

    private final Map<Integer, PlayerBuildingManager> managerMap = Maps.newConcurrentMap();

    @Override
    public PlayerBuilding getBuilding(int playerId, int buildingId) {
        final QueryWrapper<PlayerBuilding> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId);
        qw.eq("building_id", buildingId);
        return getOne(qw);
    }

    @Override
    public OpResult upgrade(int playerId, int buildingId) {
        Building building = buildingCache.get(buildingId);
        if (building == null) {
            return OpResult.fail(LocalMessages.T_BUILDING_1);
        }
        PlayerBuildingManager manager = getManager(buildingId);
        return manager.upgrade(playerId);
    }

    @Override
    public OpResult confirm(int playerId, int buildingId) {
        Building building = buildingCache.get(buildingId);
        if (building == null) {
            return OpResult.fail(LocalMessages.T_BUILDING_1);
        }
        PlayerBuildingManager manager = getManager(buildingId);
        return manager.confirm(playerId);
    }

    @Override
    public PlayerBuildingManager getManager(int buildingId) {
        return managerMap.computeIfAbsent(buildingId, (k) -> new PlayerBuildingManager(buildingId));
    }

    @Override
    public void handleLogin(int playerId) {
        QueryWrapper<PlayerBuilding> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId);
        List<Map<String, Object>> jsonList = Lists.newLinkedList();
        List<PlayerBuilding> pbs = this.list(qw);
        for (PlayerBuilding pb : pbs) {
            final int buildingId = pb.getBuildingId();
            PlayerBuildingManager manager = getManager(buildingId);
            manager.handleLogin(pb);
            jsonList.add(manager.getBuildingJson(pb));
        }
        Players.push(playerId, PushCommand.BUILDING_ALL_INFO, jsonList);
    }


}
