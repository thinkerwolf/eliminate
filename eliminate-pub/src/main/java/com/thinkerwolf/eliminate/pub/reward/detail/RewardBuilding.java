package com.thinkerwolf.eliminate.pub.reward.detail;

import com.thinkerwolf.eliminate.pub.reward.AbstractRewardInteger;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;

public class RewardBuilding extends AbstractRewardInteger {

    public RewardBuilding(int id) {
        super(id);
    }

    @Override
    public RewardType getRewardType() {
        return RewardType.BUILDING;
    }

    @Override
    public String getName() {
        return SDataBusManager.getDataGetter().getBuildingCache().get(getValue()).getName();
    }
}
