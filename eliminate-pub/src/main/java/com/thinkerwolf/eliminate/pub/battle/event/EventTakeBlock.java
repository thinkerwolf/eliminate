package com.thinkerwolf.eliminate.pub.battle.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.BattleEvent;
import com.thinkerwolf.eliminate.pub.battle.map.BlockLayer;
import com.thinkerwolf.eliminate.pub.battle.map.IBlock;
import com.thinkerwolf.eliminate.pub.battle.player.BattlePlayer;
import com.thinkerwolf.eliminate.common.PushCommand;

import java.util.Map;

public class EventTakeBlock extends BattleEvent {
    private final Battle battle;
    private final int playerId;
    private final int layer;
    private final int y;
    private final int x;

    public EventTakeBlock(Battle battle, int playerId, int layer, int y, int x) {
        this.battle = battle;
        this.playerId = playerId;
        this.layer = layer;
        this.y = y;
        this.x = x;
    }

    @Override
    protected void doRun() {
        BattlePlayer bp = battle.getPlayerManager().getPlayer(playerId);
        if (bp == null) {
            return;
        }
        BlockLayer blockLayer = battle.getBattleMap().getLayer(layer);
        IBlock block = blockLayer.getBlock(y, x);
        if (block == null) {
            return;
        }
        if (battle.getBattleMap().isCovered(layer, y, x)) {
            return;
        }
        blockLayer.removeBlock(block.getY(), block.getX());
        bp.addBlock(block);

        Map<String, Object> data = Maps.newHashMap();
        data.put("pid", playerId);
        data.put("takes", Sets.newHashSet(block));
        battle.pushToAll(PushCommand.BATTLE_TAKE_BLOCK, data);

        bp.checkAndOver();
    }
}
