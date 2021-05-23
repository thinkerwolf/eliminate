package com.thinkerwolf.eliminate.pub.reward;

public abstract class AbstractRewardIdNum extends Reward {
    private final int id;
    private final int num;

    public AbstractRewardIdNum(int id, int num) {
        this.id = id;
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }
}
