package com.thinkerwolf.eliminate.building.comm;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.building.entity.PlayerBuilding;
import com.thinkerwolf.eliminate.building.service.IPlayerBuildingService;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.EliminateConstants;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.Building;
import com.thinkerwolf.eliminate.pub.sdata.entity.BuildingLv;
import com.thinkerwolf.eliminate.pub.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 玩家建筑管理器
 *
 * @author wukai
 */
public class PlayerBuildingManager {

    private final int buildingId;

    private final IPlayerBuildingService playerBuildingService;

    /**
     * playerId -> future
     */
    private final Map<Integer, ScheduledFuture<?>> upgradeFutures = Maps.newConcurrentMap();

    public PlayerBuildingManager(int buildingId) {
        this.buildingId = buildingId;
        this.playerBuildingService = DataBusManager.getDataBus().getPlayerBuildingService();
    }

    public OpResult upgrade(final int playerId) {
        final PlayerBuilding pb = playerBuildingService.getBuilding(playerId, buildingId);
        if (pb == null) {
            return OpResult.fail(LocalMessages.T_BUILDING_0);
        }
        if (BuildingState.idOf(pb.getState()) == BuildingState.BUILDING) {
            return OpResult.fail(LocalMessages.T_BUILDING_2);
        }
        if (upgradeFutures.containsKey(playerId)) {
            return OpResult.fail(LocalMessages.T_BUILDING_2);
        }
        BuildingLv buildingLv = SDataBusManager.getDataGetter().getBuildingLvCache().getBuildingLv(buildingId, pb.getLv() + 1);
        if (buildingLv == null) {
            return OpResult.fail(LocalMessages.T_BUILDING_3);
        }

        pb.setState(BuildingState.BUILDING.getId());
        pb.setUpdateTime(new Date());
        playerBuildingService.saveOrUpdate(pb);

        Players.push(playerId, PushCommand.BUILDING_INFO, getBuildingJson(pb));
        upgradeFutures.put(playerId, addBuildingTask(pb, buildingLv.getCd() * 1000));

        return OpResult.ok();
    }

    /**
     * 建筑直接升级
     *
     * @param playerId
     * @return
     */
    public OpResult directUpgrade(final int playerId, int lv) {
        final PlayerBuilding pb = playerBuildingService.getBuilding(playerId, buildingId);
        if (pb == null) {
            return OpResult.fail(LocalMessages.T_BUILDING_0);
        }
        ScheduledFuture<?> future = upgradeFutures.get(playerId);
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
        for (int i = 1; i <= lv; i++) {
            BuildingLv buildingLv = SDataBusManager.getDataGetter().getBuildingLvCache().getBuildingLv(buildingId, pb.getLv() + 1);
            if (buildingLv == null) {
                return OpResult.fail(LocalMessages.T_BUILDING_3);
            }
            pb.setLv(pb.getLv() + 1);
        }
        pb.setState(BuildingState.IDLE.getId());
        pb.setUpdateTime(new Date());
        playerBuildingService.saveOrUpdate(pb);
        Players.push(playerId, PushCommand.BUILDING_INFO, getBuildingJson(pb));
        return OpResult.ok();
    }

    public OpResult confirm(final int playerId) {
        final PlayerBuilding pb = playerBuildingService.getBuilding(playerId, buildingId);
        if (pb == null) {
            return OpResult.fail(LocalMessages.T_BUILDING_0);
        }
        BuildingState bs = BuildingState.idOf(pb.getState());
        if (bs != BuildingState.CONFIRM) {
            return OpResult.fail(LocalMessages.T_BUILDING_5);
        }
        pb.setState(BuildingState.IDLE.getId());
        pb.setUpdateTime(new Date());
        playerBuildingService.saveOrUpdate(pb);
        Players.push(playerId, PushCommand.BUILDING_INFO, getBuildingJson(pb));
        return OpResult.ok();
    }

    /**
     * 减少建筑升级CD
     *
     * @param playerId
     * @param cd
     * @return
     */
    public OpResult reduceCd(final int playerId, int cd) {
        final PlayerBuilding pb = playerBuildingService.getBuilding(playerId, buildingId);
        if (pb == null) {
            return OpResult.fail(LocalMessages.T_BUILDING_0);
        }
        ScheduledFuture<?> future = upgradeFutures.get(playerId);
        if (future == null || future.isDone()) {
            return OpResult.fail(LocalMessages.T_BUILDING_4);
        }
        long remain = future.getDelay(TimeUnit.MILLISECONDS);
        long newRemain = remain - TimeUnit.SECONDS.toMillis(cd);
        if (newRemain <= 0) {
            ((RunnableScheduledFuture<?>) future).run();
        } else {
            future.cancel(true);
            pb.setUpdateTime(DateUtil.reduceTime(pb.getUpdateTime(), cd, TimeUnit.SECONDS));
            playerBuildingService.saveOrUpdate(pb);
            upgradeFutures.put(playerId, addBuildingTask(pb, newRemain));
            Players.push(playerId, PushCommand.BUILDING_INFO, getBuildingJson(pb));
        }
        return OpResult.ok();
    }

    public void handleLogin(PlayerBuilding pb) {
        BuildingState state = BuildingState.idOf(pb.getState());
        int playerId = pb.getPlayerId();
        if (state == BuildingState.BUILDING) {
            BuildingLv nextLv = SDataBusManager.getDataGetter().getBuildingLvCache().getBuildingLv(buildingId, pb.getLv() + 1);
            upgradeFutures.computeIfAbsent(playerId, k -> {
                long remain = DateUtil.getRemain(pb.getUpdateTime(), nextLv.getCd() * 1000, TimeUnit.MILLISECONDS);
                if (remain < 500) { // 小于500ms直接完成
                    pb.setState(BuildingState.CONFIRM.getId());
                    pb.setLv(pb.getLv() + 1);
                    pb.setUpdateTime(new Date());
                    playerBuildingService.saveOrUpdate(pb);
                    return null;
                }
                return addBuildingTask(pb, remain);
            });
        }
//        Players.push(playerId, PushCommand.BUILDING_INFO, getBuildingJson(pb));
    }


    private ScheduledFuture<?> addBuildingTask(PlayerBuilding pb, long remain) {
        return EliminateConstants.COMMON_SCHEDULED.schedule(() -> {
            upgradeFutures.remove(pb.getPlayerId());
            pb.setLv(pb.getLv() + 1);
            pb.setUpdateTime(new Date());
            pb.setState(BuildingState.CONFIRM.getId());
            playerBuildingService.saveOrUpdate(pb);
            Players.push(pb.getPlayerId(), PushCommand.BUILDING_INFO, getBuildingJson(pb));

        }, remain, TimeUnit.MILLISECONDS);
    }

    public Map<String, Object> getBuildingJson(PlayerBuilding pb) {
        HashMap<String, Object> map = Maps.newHashMap();
        Building building = SDataBusManager.getDataGetter().getBuildingCache().get(buildingId);
        map.put("buildingId", buildingId);
        map.put("name", building.getName());
        map.put("lv", pb.getLv());        // 0级代表可建造
        map.put("state", pb.getState());
        BuildingLv curLv = SDataBusManager.getDataGetter().getBuildingLvCache().getBuildingLv(buildingId, pb.getLv());
        map.put("pic", curLv.getPic());
        map.put("scale", curLv.getScale());
        BuildingState state = BuildingState.idOf(pb.getState());
        if (state == BuildingState.BUILDING) {
            BuildingLv nextlv = SDataBusManager.getDataGetter().getBuildingLvCache().getBuildingLv(buildingId, pb.getLv() + 1);
            map.put("tcd", nextlv.getCd());
            long rcd = DateUtil.getRemain(pb.getUpdateTime().getTime() + 500, nextlv.getCd() * 1000, TimeUnit.SECONDS);
            map.put("rcd", rcd);
        }
        return map;
    }

    public void pushInfo(PlayerBuilding pb) {
        Players.push(pb.getPlayerId(), PushCommand.BUILDING_INFO, getBuildingJson(pb));
    }

}
