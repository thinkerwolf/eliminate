package com.thinkerwolf.eliminate.player.comm;

import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardVit;

/**
 * 体力恢复任务
 *
 * @author wukai
 */
public class VitRecoverTask implements Runnable {

    private volatile boolean stop;

    private final int playerId;
    private final long executionTime;

    public VitRecoverTask(int playerId, long delay) {
        this.playerId = playerId;
        this.executionTime = System.currentTimeMillis() + delay;
    }

    @Override
    public void run() {
        if (stop) {
            return;
        }
        RewardVit rv = new RewardVit(1);
        RewardManager.addReward(playerId, rv, "");
    }

    public long getDelay() {
        return Math.max(0, executionTime - System.currentTimeMillis());
    }

    public void cancel() {
        stop = true;
    }

}
