package com.thinkerwolf.eliminate.game.gm.service.impl;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.game.gm.comm.GmCommand;
import com.thinkerwolf.eliminate.game.gm.service.IGmService;
import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.BattleManager;
import com.thinkerwolf.eliminate.pub.battle.event.EventBattleEnd;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.ScriptFactory;
import com.thinkerwolf.eliminate.pub.reward.Reward;
import com.thinkerwolf.eliminate.pub.reward.RewardGroup;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.script.MapScriptInfoFactory;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.SDataBus;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfoFactory;
import com.thinkerwolf.eliminate.game.task.diary.PlayerDiarys;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service("gmService")
public class GmServiceImpl implements IGmService {
    @Override
    public OpResult help() {
        Map<String, Object> data = Maps.newHashMap();
        data.put("cmds", GmCommand.values());
        return OpResult.ok(data);
    }

    @Override
    public OpResult sendReward(int playerId, String reward) {
        List<Reward> list = ScriptFactory.buildList(reward, Reward.class);
        RewardGroup group = new RewardGroup(list);
        RewardManager.checkAndAddReward(playerId, group, "GM");
        return OpResult.ok();
    }

    @Override
    public OpResult consumeReward(int playerId, String reward) {
        RewardGroup group = RewardGroup.group(reward);
        RewardManager.checkConsumeReward(playerId, group, "GM");
        return OpResult.ok();
    }

    @Override
    public OpResult stopBattle(int playerId) {
        Battle battle = BattleManager.getBattle(playerId);
        if (battle != null) {
            BattleManager.removeBattle(playerId);
            battle.getExecutor().addEvent(new EventBattleEnd(0, battle));
        }
        return OpResult.ok();
    }

    @Override
    public OpResult refreshSdata(int playerId, String[] ps) {
        SDataBus bus = SDataBusManager.getDataGetter();
        Method[] methods = bus.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                try {
                    Object obj = method.invoke(bus);
                    if (obj instanceof AbstractCache) {
                        ((AbstractCache) obj).afterPropertiesSet();
                    }
                } catch (Exception ignored) {
                }
            }
        }
        MapScriptInfoFactory.clear();
        TaskInfoFactory.init();
        return OpResult.ok();
    }

    @Override
    public OpResult action(int playerId, String[] ps) {
        int groupId = 1;
        if (ps.length > 0 && StringUtils.isNotBlank(ps[0])) {
            groupId = Integer.parseInt(ps[0]);
        }
        PlayerDiarys.pushAction(playerId, groupId);
        return OpResult.ok();
    }
}
