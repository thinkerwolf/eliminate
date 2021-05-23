package com.thinkerwolf.eliminate.pub.battle;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.player.Players;

import java.util.Map;

/**
 * 战斗管理器
 *
 * @author wukai
 */
public final class BattleManager {

    private static final Map<Integer, Battle> playerBattles = Maps.newConcurrentMap();

    private static IBattleHandler battleHandler;

    public static void setBattleHandler(IBattleHandler battleHandler) {
        BattleManager.battleHandler = battleHandler;
    }

    public static IBattleHandler getBattleHandler() {
        return battleHandler;
    }

    public static Battle getBattle(int playerId) {
        return playerBattles.get(playerId);
    }

    public static void setBattle(int playerId, Battle battle) {
        playerBattles.put(playerId, battle);
    }

    public static void removeBattle(int playerId) {
        playerBattles.remove(playerId);
    }

    public static void handleLogin(int playerId) {
        Battle battle = getBattle(playerId);
        if (battle != null) {
            Map<String, Object> data = Maps.newHashMapWithExpectedSize(1);
            data.put("battleId", battle.getId());
            Players.push(playerId, PushCommand.BATTLE_INFO, data);
        }
    }

}
