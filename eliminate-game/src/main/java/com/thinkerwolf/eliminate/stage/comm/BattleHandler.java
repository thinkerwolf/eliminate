package com.thinkerwolf.eliminate.stage.comm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.bag.entity.PlayerBag;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.IBattleHandler;
import com.thinkerwolf.eliminate.pub.battle.Prop;
import com.thinkerwolf.eliminate.pub.battle.PropType;
import com.thinkerwolf.eliminate.pub.reward.*;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.Stage;
import com.thinkerwolf.eliminate.pub.trigger.TriggerEvent;
import com.thinkerwolf.eliminate.pub.trigger.TriggerManager;
import com.thinkerwolf.eliminate.pub.trigger.TriggerType;
import com.thinkerwolf.eliminate.stage.entity.PlayerStage;
import com.thinkerwolf.eliminate.task.diary.PlayerDiaryTask;
import com.thinkerwolf.eliminate.task.diary.PlayerDiaryTaskManager;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BattleHandler implements IBattleHandler {
    @Override
    public List<Prop> getProps(int playerId, PropType[] propTypes) {
        List<Prop> ps = Lists.newArrayList();
        for (PropType pt : propTypes) {
            PlayerBag pb = DataBusManager.getDataBus().getPlayerBagService().getBag(playerId, RewardType.PROP.getResId(), pt.getId());
            Prop p = new Prop(pt.getId(), pb == null ? 0 : pb.getNum());
            ps.add(p);
        }
        return ps;
    }

    @Override
    public void updateStage(int playerId, Battle battle) {
        PlayerStage ps = DataBusManager.getDataBus().getPlayerStageService().getStageSafty(playerId, battle.getStageId());
        boolean win = battle.getPlayerManager().getPlayer(playerId).isWin();
        ps.setState(win ? 2 : 1);
        DataBusManager.getDataBus().getPlayerStageService().saveOrUpdate(ps);
        TriggerManager.notify(playerId, TriggerType.STAGE, new TriggerEvent(playerId));
    }

    @Override
    public Integer getNextStage(int playerId, int curStageId) {
        PlayerDiaryTask task = PlayerDiaryTaskManager.getInstance().getCurTask(playerId);
        if (task == null) {
            return 0;
        }
        return task.getCurStageId();
    }

    @Override
    public Map<String, Object> nextStageDoc(int playerId, int curStageId) {
        PlayerDiaryTask task = PlayerDiaryTaskManager.getInstance().getCurTask(playerId);
        if (task == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> doc = Maps.newHashMapWithExpectedSize(3);
        doc.put("stageId", task.getCurStageId());
        doc.put("left", task.getLeftStages());
        doc.put("kw", task.getTaskInfo().getTask().getKeyword());
        return doc;
    }
}
