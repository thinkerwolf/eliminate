package com.thinkerwolf.eliminate.reward;

import com.thinkerwolf.eliminate.common.MiscKey;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.pub.reward.AbstractRewardIdNum;
import com.thinkerwolf.eliminate.pub.reward.Processor;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;

@Processor(RewardType.STAR)
public class RewardProcessorStar extends AbstractBagRewardProcessor {

    @Override
    public void add(Integer target, AbstractRewardIdNum reward, String addKey) {
        super.add(target, reward, addKey);
        DataBusManager.getDataBus().getPlayerService().pushStar(target);
    }

    @Override
    public boolean checkAdd(Integer target, AbstractRewardIdNum reward, String addKey) {
        int starMax = SDataBusManager.getDataGetter().getMiscCache().getInt(MiscKey.STAR_MAX);
        return reward.getNum() < starMax;
    }
}
