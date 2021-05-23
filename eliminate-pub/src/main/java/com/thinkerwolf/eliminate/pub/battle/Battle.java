package com.thinkerwolf.eliminate.pub.battle;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.pub.battle.map.BattleMap;
import com.thinkerwolf.eliminate.pub.battle.map.BattleMapFactory;
import com.thinkerwolf.eliminate.pub.battle.player.BattlePlayer;
import com.thinkerwolf.eliminate.pub.battle.player.BattlePlayerManager;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfo;
import com.thinkerwolf.eliminate.pub.player.Players;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.*;

public class Battle {

    private final String id;

    private final BattleMap battleMap;

    private final BattleExecutor executor;

    private final BattlePlayerManager playerManager;

    private volatile boolean stop;

    private final int stageId;

    public Battle(int stageId, int scriptId, List<PlayerRoleInfo> oneRoles) {
        this.stageId = stageId;
        this.battleMap = BattleMapFactory.create(scriptId);
        this.playerManager = new BattlePlayerManager(this);
        this.executor = new BattleExecutor();
        for (PlayerRoleInfo roleInfo : oneRoles) {
            BattlePlayer bp = new BattlePlayer(this, roleInfo);
            playerManager.addPlayer(bp);
        }
        this.id = generateId(scriptId);
    }

    public String getId() {
        return id;
    }

    public int getStageId() {
        return stageId;
    }

    public BattleMap getBattleMap() {
        return battleMap;
    }

    public BattleExecutor getExecutor() {
        return executor;
    }

    public BattlePlayerManager getPlayerManager() {
        return playerManager;
    }

    private String generateId(int scriptId) {
        StringBuilder sb = new StringBuilder();
        sb.append("battle_").append(scriptId).append(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS"));
        Collection<BattlePlayer> bps = playerManager.getAllPlayer();
        for (BattlePlayer bp : bps) {
            sb.append("_").append(bp.getPlayerId());
        }
        return sb.toString();
    }

    public void view(int playerId) {
        executor.addEvent(new BattleEvent() {
            @Override
            protected void doRun() {
                BattlePlayer bp = playerManager.getPlayer(playerId);
                Players.push(playerId, PushCommand.BATTLE_INI, buildJson(playerId));
            }
        });
    }

    public void pushToAll(PushCommand command, Object data) {
        for (BattlePlayer bp : playerManager.getAllPlayer()) {
            Players.push(bp.getPlayerId(), command, data);
        }
    }

    public void pushToOne(int playerId, PushCommand command, Object data) {
        Players.push(playerId, command, data);
    }

    public Object buildJson(int playerId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("battleId", id);
        map.put("layers", battleMap.getLayers());
        map.put("players", playerManager.getAllPlayer());
        BattlePlayer bp = getPlayerManager().getPlayer(playerId);
        if (bp != null) {
            map.put("props", BattleManager.getBattleHandler().getProps(playerId, PropType.values()));
        }
        return map;
    }

    public void setStop() {
        this.stop = true;
    }

    public boolean isStop() {
        return stop;
    }

    public void destroy() {
        try {
            battleMap.destroy();
        } catch (Exception ignored) {
        }
        try {
            playerManager.destroy();
        } catch (Exception ignored) {
        }
    }


}
