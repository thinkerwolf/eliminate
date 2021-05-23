package com.thinkerwolf.eliminate.game.config;

import com.thinkerwolf.eliminate.common.EliminateConstants;
import com.thinkerwolf.eliminate.common.config.AbstractInitManager;
import com.thinkerwolf.eliminate.game.databus.DataBus;
import com.thinkerwolf.eliminate.game.databus.DataBusManager;
import com.thinkerwolf.eliminate.pub.battle.BattleManager;
import com.thinkerwolf.eliminate.pub.battle.IBattleHandler;
import com.thinkerwolf.eliminate.pub.reward.IRewardProcessor;
import com.thinkerwolf.eliminate.pub.reward.Processor;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.sdata.SDataBus;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@SuppressWarnings("rawtypes")
public class InitManagerImpl extends AbstractInitManager {

    @Autowired
    DataBus dataBus;
    @Autowired
    SDataBus sDataBus;
    @Autowired
    IBattleHandler battleHandler;

    @Override
    protected void doInit() throws Exception {
        DataBusManager.init(dataBus);
        SDataBusManager.init(sDataBus);
        BattleManager.setBattleHandler(battleHandler);
        initRewardProcessors();
        TaskInfoFactory.init();
        EliminateConstants.GAME_ID = getGamerMyId();
    }


    private void initRewardProcessors() {
        Map<String, Object> map = getApplicationContext().getBeansWithAnnotation(Processor.class);
        IRewardProcessor[] processorArr = new IRewardProcessor[RewardType.values().length];
        for (Object v : map.values()) {
            if (v instanceof IRewardProcessor) {
                IRewardProcessor processor = (IRewardProcessor) v;
                Processor annotation = processor.getClass().getAnnotation(Processor.class);
                int pos = annotation.value().ordinal();
                processorArr[pos] = processor;
            }
        }
        RewardManager.init(processorArr);
    }

}
