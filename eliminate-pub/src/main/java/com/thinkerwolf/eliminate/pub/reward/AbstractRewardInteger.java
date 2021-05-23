package com.thinkerwolf.eliminate.pub.reward;

public abstract class AbstractRewardInteger extends Reward {

    private int value;

    public AbstractRewardInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
