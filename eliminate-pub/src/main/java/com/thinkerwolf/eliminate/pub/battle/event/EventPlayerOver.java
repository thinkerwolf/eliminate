package com.thinkerwolf.eliminate.pub.battle.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.BattleEvent;
import com.thinkerwolf.eliminate.pub.battle.BattleManager;
import com.thinkerwolf.eliminate.pub.battle.PropType;
import com.thinkerwolf.eliminate.pub.battle.player.BattlePlayer;
import com.thinkerwolf.eliminate.pub.battle.player.PlayerState;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.reward.RewardGroup;
import com.thinkerwolf.eliminate.pub.reward.RewardKey;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.Stage;

import java.util.Map;

public class EventPlayerOver extends BattleEvent {
    private final Battle battle;
    private final int playerId;
    private final boolean win;

    public EventPlayerOver(long delay, Battle battle, int playerId, boolean win) {
        super(delay);
        this.battle = battle;
        this.playerId = playerId;
        this.win = win;
    }

    @Override
    protected void doRun() {
        BattlePlayer bp = battle.getPlayerManager().getPlayer(playerId);
        bp.setState(win ? PlayerState.WIN : PlayerState.LOSE);

        boolean battleEnd;
        if (win) {
            battleEnd = true;
            for (BattlePlayer obp : battle.getPlayerManager().getAllPlayer()) {
                BattleManager.removeBattle(playerId);
                if (obp.getPlayerId() != playerId) {
                    obp.setState(PlayerState.LOSE);
                }
                handlePlayer(obp, bp);
            }
        } else {
            battleEnd = battle.getPlayerManager().getRemainPlayerNum() <= 0;
            BattleManager.removeBattle(playerId);
            handlePlayer(bp, bp);
        }

        if (battleEnd) {
            battle.getExecutor().addEvent(new EventBattleEnd(2000, battle));
        }

    }

    private void handlePlayer(BattlePlayer bp, BattlePlayer winBp) {
        boolean win = bp.getState() == PlayerState.WIN;
        BattleManager.getBattleHandler().updateStage(bp.getPlayerId(), battle);
        Map<String, Object> data = Maps.newHashMapWithExpectedSize(5);
        data.put("battleId", battle.getId());
        data.put("win", win);
        if (winBp != bp) {
            data.put("winPid", winBp.getPlayerId());
        }
        if (win) {
            if (bp.getUsedProps().size() >= 2) {
                int s = bp.getUsedProps().size();
                data.put("extra", Lists.newArrayList(bp.getUsedProps().get(s - 1).getId(), bp.getUsedProps().get(s - 2).getId()));
            } else {
                data.put("extra", Lists.newArrayList(PropType.WITHDRAW.getId(), PropType.WITHDRAW.getId()));
            }
//            data.put("next", BattleManager.getBattleHandler().nextStageDoc(playerId, battle.getStageId()));
            Stage stage = SDataBusManager.getDataGetter().getStageCache().get(battle.getStageId());
            RewardGroup group = RewardGroup.group(stage.getReward());
            RewardManager.checkAddReward(playerId, group, RewardKey.BATTLE);
            data.put("rewards", group.getRewards());
        }
        battle.pushToOne(bp.getPlayerId(), PushCommand.BATTLE_END, data);
    }

}
