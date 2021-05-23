package com.thinkerwolf.eliminate.pub.reward.detail;

import com.thinkerwolf.eliminate.pub.reward.AbstractRewardIdNum;
import com.thinkerwolf.eliminate.pub.reward.RewardType;

public class RewardBuildingCd extends AbstractRewardIdNum {
    public RewardBuildingCd(int id, int num) {
        super(id, num);
    }

    @Override
    public RewardType getRewardType() {
        return RewardType.BUILDING_CD;
    }
}
