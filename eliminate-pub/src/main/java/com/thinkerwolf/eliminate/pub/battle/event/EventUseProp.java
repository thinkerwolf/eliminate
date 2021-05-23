package com.thinkerwolf.eliminate.pub.battle.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thinkerwolf.eliminate.pub.battle.PropType;
import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.BattleEvent;
import com.thinkerwolf.eliminate.pub.battle.map.BlockLayer;
import com.thinkerwolf.eliminate.pub.battle.map.IBlock;
import com.thinkerwolf.eliminate.pub.battle.player.BattlePlayer;
import com.thinkerwolf.eliminate.common.PushCommand;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.thinkerwolf.eliminate.pub.battle.BattleConstants.ERASURE_NUM;

public class EventUseProp extends BattleEvent {
    private final Battle battle;
    private final int playerId;
    private final PropType propType;

    public EventUseProp(long delay, Battle battle, int playerId, PropType propType) {
        super(delay);
        this.battle = battle;
        this.playerId = playerId;
        this.propType = propType;
    }

    @Override
    protected void doRun() {
        if (battle.isStop()) {
            return;
        }
        BattlePlayer bp = battle.getPlayerManager().getPlayer(playerId);
        bp.addUsedProp(propType);
        switch (propType) {
            case WITHDRAW:
                withdraw(bp);
                break;
            case ELIMINATE:
                eliminate(bp);
                break;
            case SHUFFLE:
                shuffle(bp);
                break;
        }
    }

    private void withdraw(BattlePlayer bp) {
        IBlock block = bp.popBlock();
        Map<String, Object> data = Maps.newHashMapWithExpectedSize(5);
        Set<IBlock> backs = Collections.emptySet();
        if (block != null) {
            BlockLayer bl = battle.getBattleMap().getLayer(block.getLayer());
            bl.setBlock(block);
            backs = Sets.newHashSet(block);
        }
        data.put("pid", bp.getPlayerId());
        data.put("backs", backs);
        battle.pushToAll(PushCommand.BATTLE_BACK_BLOCK, data);
    }

    private void eliminate(BattlePlayer bp) {
        boolean done = false;
        Map<String, Integer> cm = bp.getCheckMap();
        Set<IBlock> result = null;
        for (Map.Entry<String, Integer> e : cm.entrySet()) {
            int exceptedNum = Math.min(ERASURE_NUM, ERASURE_NUM - e.getValue());
            Set<IBlock> bs = battle.getBattleMap().findBlocks(e.getKey(), exceptedNum);
            if (bs.size() >= exceptedNum) {
                done = true;
                bp.addBlocks(bs);
                battle.getBattleMap().removeBlocks(bs);
                result = bs;
                break;
            }
        }
        if (!done) {
            for (String pic : battle.getBattleMap().getPics()) {
                Set<IBlock> bs = battle.getBattleMap().findBlocks(pic, ERASURE_NUM);
                if (bs.size() >= ERASURE_NUM) {
                    bp.addBlocks(bs);
                    battle.getBattleMap().removeBlocks(bs);
                    result = bs;
                    break;
                }
            }
        }
        Map<String, Object> data = Maps.newHashMap();
        data.put("pid", bp.getPlayerId());
        data.put("takes", result == null ? Collections.emptySet() : result);
        battle.pushToAll(PushCommand.BATTLE_TAKE_BLOCK, data);

        bp.checkAndOver();
    }

    private void shuffle(BattlePlayer bp) {
        battle.getBattleMap().shuffleBlocks();
        Map<String, Object> data = Maps.newHashMap();
        data.put("pid", bp.getPlayerId());
        data.put("layers", battle.getBattleMap().getLayers());
        battle.pushToAll(PushCommand.BATTLE_SHUFFLE_BLOCK, data);
    }

}
