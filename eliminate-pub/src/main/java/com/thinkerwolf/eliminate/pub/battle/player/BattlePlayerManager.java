package com.thinkerwolf.eliminate.pub.battle.player;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.pub.battle.Battle;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BattlePlayerManager {

    private final Map<Integer, BattlePlayer> players = Maps.newConcurrentMap();

    private final Battle battle;

    public BattlePlayerManager(Battle battle) {
        this.battle = battle;
    }

    public void addPlayer(BattlePlayer bp) {
        players.put(bp.getPlayerId(), bp);
    }

    public Collection<BattlePlayer> getAllPlayer() {
        return players.values();
    }

    public BattlePlayer getPlayer(int playerId) {
        return players.get(playerId);
    }

    public void removePlayer(int playerId) {
        players.remove(playerId);
    }

    public int getRemainPlayerNum() {
        final AtomicInteger a = new AtomicInteger(0);
        players.forEach((pid, bp) -> {
            if (!bp.isLose()) {
                a.incrementAndGet();
            }
        });
        return a.intValue();
    }

    public void destroy() {
        players.clear();
    }
}
