package com.thinkerwolf.eliminate.pub.reward.detail;

import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.pub.reward.AbstractRewardIdNum;
import com.thinkerwolf.eliminate.pub.reward.RewardType;

public class RewardStar extends AbstractRewardIdNum {

    public RewardStar(int num) {
        super(0, num);
    }

    @Override
    public RewardType getRewardType() {
        return RewardType.STAR;
    }

    @Override
    public String getName() {
        return LocalMessages.N_STAR;
    }
}
