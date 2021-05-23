package com.thinkerwolf.eliminate.pub.reward;


import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.SeprationLock;

/**
 * 奖励管理器
 *
 * @author wukai
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RewardManager {

    private static IRewardProcessor[] rewardProcessors;

    private static SeprationLock locks = new SeprationLock(1024);

    public static void init(IRewardProcessor[] processors) {
        rewardProcessors = processors;
    }


    public static OpResult addRewardGroup(Object target, RewardGroup rewardGroup, String key) {
        synchronized (locks.getLock(target)) {
            for (Reward reward : rewardGroup.getRewards()) {
                rewardProcessors[reward.getRewardType().ordinal()].add(target, reward, key);
            }
            return OpResult.ok();
        }
    }

    public static OpResult addReward(Object target, Reward reward, String key) {
        synchronized (locks.getLock(target)) {
            rewardProcessors[reward.getRewardType().ordinal()].add(target, reward, key);
            return OpResult.ok();
        }
    }

    public static OpResult checkAndAddReward(Object target, RewardGroup rewardGroup, String key) {
        synchronized (locks.getLock(target)) {
            OpResult op = checkAddReward(target, rewardGroup, key);
            if (!op.isOk()) {
                return op;
            }
            addRewardGroup(target, rewardGroup, key);
        }
        return OpResult.ok();
    }

    public static OpResult checkAndAddReward(Object target, Reward reward, String key) {
        synchronized (locks.getLock(target)) {
            OpResult op = checkAddReward(target, reward, key);
            if (!op.isOk()) {
                return op;
            }
            addReward(target, reward, key);
        }
        return OpResult.ok();
    }

    public static OpResult checkAddReward(Object target, RewardGroup rewardGroup, String key) {
        for (Reward reward : rewardGroup.getRewards()) {
            OpResult op = checkAddReward(target, reward, key);
            if (!op.isOk()) {
                return op;
            }
        }
        return OpResult.ok();
    }

    public static OpResult checkAddReward(Object target, Reward reward, String key) {
        synchronized (locks.getLock(target)) {
            boolean suc = rewardProcessors[reward.getRewardType().ordinal()].checkAdd(target, reward, key);
            if (!suc) {
                return OpResult.fail(key);
            }
            return addReward(target, reward, key);
        }
    }


    public static OpResult consumeRewardGroup(Object target, RewardGroup rewardGroup, String key) {
        synchronized (locks.getLock(target)) {
            OpResult op = checkConsumeReward(target, rewardGroup, key);
            if (!op.isOk()) {
                return op;
            }
            for (Reward reward : rewardGroup.getRewards()) {
                consumeReward(target, reward, key);
            }
            return OpResult.ok();
        }
    }

    public static OpResult consumeReward(Object target, Reward reward, String key) {
        synchronized (locks.getLock(target)) {
            OpResult op = checkConsumeReward(target, reward, key);
            if (!op.isOk()) {
                return op;
            }
            rewardProcessors[reward.getRewardType().ordinal()].consume(target, reward, key);
        }
        return OpResult.ok();
    }

    public static OpResult checkConsumeReward(Object target, RewardGroup rewardGroup, String key) {
        for (Reward reward : rewardGroup.getRewards()) {
            OpResult op = checkConsumeReward(target, reward, key);
            if (!op.isOk()) {
                return op;
            }
        }
        return OpResult.ok();
    }

    public static OpResult checkConsumeReward(Object target, Reward reward, String key) {
        synchronized (locks.getLock(target)) {
            IRewardProcessor processor = rewardProcessors[reward.getRewardType().ordinal()];
            boolean suc = processor.checkConsume(target, reward, key);
            if (!suc) {
                return OpResult.fail(key);
            }
            processor.consume(target, reward, key);
            return OpResult.ok();
        }
    }

}
