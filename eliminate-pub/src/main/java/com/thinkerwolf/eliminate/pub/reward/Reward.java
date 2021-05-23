package com.thinkerwolf.eliminate.pub.reward;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Reward {

    @JsonIgnore
    public abstract RewardType getRewardType();

    public int getResId() {
        return getRewardType().getResId();
    }

    public String getName() {
        return "";
    }

}
