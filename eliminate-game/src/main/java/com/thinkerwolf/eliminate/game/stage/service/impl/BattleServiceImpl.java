package com.thinkerwolf.eliminate.game.stage.service.impl;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.BattleManager;
import com.thinkerwolf.eliminate.pub.battle.PropType;
import com.thinkerwolf.eliminate.pub.battle.event.EventBattleEnd;
import com.thinkerwolf.eliminate.pub.battle.event.EventTakeBlock;
import com.thinkerwolf.eliminate.pub.battle.event.EventUseProp;
import com.thinkerwolf.eliminate.pub.battle.map.BlockLayer;
import com.thinkerwolf.eliminate.pub.battle.map.IBlock;
import com.thinkerwolf.eliminate.pub.battle.player.BattlePlayer;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfo;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfoManager;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardProp;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardVit;
import com.thinkerwolf.eliminate.pub.script.MapScriptInfo;
import com.thinkerwolf.eliminate.pub.script.MapScriptInfoFactory;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.Stage;
import com.thinkerwolf.eliminate.game.stage.service.IBattleService;
import com.thinkerwolf.eliminate.game.stage.service.IPlayerStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service("battleService")
public class BattleServiceImpl implements IBattleService {

    @Autowired
    IPlayerStageService playerStageService;

    @Override
    public OpResult begin(int playerId) {
        int stageId = playerStageService.getBeginStageId(playerId);
        return start(playerId, stageId);
    }

    @Override
    public OpResult start(int playerId, int stageId) {
        Stage stage = SDataBusManager.getDataGetter().getStageCache().get(stageId);
        if (stage == null) {
            return OpResult.fail(LocalMessages.T_STAGE_1);
        }
        MapScriptInfo msi = MapScriptInfoFactory.getInfo(stage.getScriptId());
        if (msi == null) {
            return OpResult.fail(LocalMessages.T_STAGE_2);
        }
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(playerId);
        if (BattleManager.getBattle(playerId) != null) {
            return OpResult.fail(LocalMessages.T_BATTLE_1);
        }
        RewardVit vit = new RewardVit(1);
        OpResult op = RewardManager.checkConsumeReward(playerId, vit, LocalMessages.T_PLAYER_2);
        if (!op.isOk()) {
            return op;
        }
        Battle battle = new Battle(stageId, msi.getScriptId(), Collections.singletonList(roleInfo));
        BattleManager.setBattle(playerId, battle);
        Map<String, Object> map = Maps.newHashMap();
        map.put("battleId", battle.getId());
        return OpResult.ok(map);
    }

    @Override
    public OpResult view(int playerId, String battleId) {
        Battle battle = BattleManager.getBattle(playerId);
        if (battle == null) {
            return OpResult.fail(LocalMessages.T_BATTLE_2);
        }
        battle.view(playerId);
        return OpResult.ok();
    }

    @Override
    public OpResult takeBlock(int playerId, int layer, int y, int x) {
        Battle battle = BattleManager.getBattle(playerId);
        if (battle == null) {
            return OpResult.fail(LocalMessages.T_BATTLE_2);
        }
        if (battle.getPlayerManager().getPlayer(playerId) == null) {
            return OpResult.fail(LocalMessages.T_BATTLE_5);
        }
        BlockLayer blockLayer = battle.getBattleMap().getLayer(layer);
        if (blockLayer == null) {
            return OpResult.fail(LocalMessages.T_BATTLE_3);
        }
        IBlock block = blockLayer.getBlock(y, x);
        if (block == null) {
            return OpResult.fail(LocalMessages.T_BATTLE_4);
        }
        if (battle.getBattleMap().isCovered(layer, y, x)) {
            return OpResult.fail(LocalMessages.T_BATTLE_6);
        }
        battle.getExecutor().addEvent(new EventTakeBlock(battle, playerId, layer, y, x));
        return OpResult.ok();
    }

    @Override
    public OpResult useProp(int playerId, int propId) {
        Battle battle = BattleManager.getBattle(playerId);
        if (battle == null || battle.isStop()) {
            return OpResult.fail(LocalMessages.T_BATTLE_2);
        }
        PropType pt = PropType.idOf(propId);
        if (pt == null) {
            return OpResult.fail(LocalMessages.T_BATTLE_8);
        }
        BattlePlayer bp = battle.getPlayerManager().getPlayer(playerId);
        if (bp == null || bp.isOver()) {
            return OpResult.fail(LocalMessages.T_BATTLE_10);
        }
        // 校验
        if (pt == PropType.WITHDRAW) {
            if (bp.isEmpty()) {
                return OpResult.fail(LocalMessages.T_BATTLE_10);
            }
        }
        RewardProp rp = new RewardProp(propId, 1);
        OpResult op = RewardManager.checkConsumeReward(playerId, rp, LocalMessages.T_BATTLE_7);
        if (!op.isOk()) {
            return op;
        }
        battle.getExecutor().addEvent(new EventUseProp(0, battle, playerId, pt));
        return OpResult.ok();
    }

    @Override
    public OpResult restart(int playerId) {
        Battle battle = BattleManager.getBattle(playerId);
        if (battle == null) {
            return OpResult.fail(LocalMessages.T_BATTLE_2);
        }
        int stageId = battle.getStageId();
        BattleManager.removeBattle(playerId);
        battle.getExecutor().addEvent(new EventBattleEnd(0, battle));
        OpResult op = start(playerId, stageId);
        view(playerId, null);
        return op;
    }

}
