package com.thinkerwolf.eliminate.pub.reward.detail;

import com.thinkerwolf.eliminate.pub.reward.AbstractRewardIdNum;
import com.thinkerwolf.eliminate.pub.reward.RewardType;

public class RewardBuildingLv extends AbstractRewardIdNum {
    public RewardBuildingLv(int id, int num) {
        super(id, num);
    }

    @Override
    public RewardType getRewardType() {
        return RewardType.BUILDING_LV;
    }
}
