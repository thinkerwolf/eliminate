package com.thinkerwolf.eliminate.pub.reward;

import com.google.common.collect.Lists;
import com.thinkerwolf.eliminate.common.ScriptFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 奖励列表
 *
 * @author wukai
 */
public class RewardGroup {

    private List<Reward> rewards;

    public RewardGroup(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public Reward getReward(int pos) {
        if (pos < 0 || pos > rewards.size() - 1) {
            return null;
        }
        return rewards.get(pos);
    }

    public void addReward(Reward reward) {
        this.rewards.add(reward);
    }

    public void addReward(RewardGroup rewardGroup) {
        this.rewards.addAll(rewardGroup.rewards);
    }

    public static RewardGroup group(String string) {
        return new RewardGroup(ScriptFactory.buildList(string, Reward.class));
    }

    public static RewardGroup group(Reward... rewards) {
        List<Reward> list = Lists.newArrayList();
        list.addAll(Arrays.asList(rewards));
        return new RewardGroup(list);
    }
}
