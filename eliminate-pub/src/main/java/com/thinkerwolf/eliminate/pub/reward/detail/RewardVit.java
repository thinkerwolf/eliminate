package com.thinkerwolf.eliminate.pub.reward.detail;

import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.pub.reward.AbstractRewardInteger;
import com.thinkerwolf.eliminate.pub.reward.RewardType;

public class RewardVit extends AbstractRewardInteger {

    public RewardVit(int value) {
        super(value);
    }

    @Override
    public RewardType getRewardType() {
        return RewardType.VIT;
    }

    @Override
    public String getName() {
        return LocalMessages.N_VIT;
    }
}
