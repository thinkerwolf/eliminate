package com.thinkerwolf.eliminate.pub.reward;

/**
 * @author wukai
 * @since 2020-05-24
 */
public interface IRewardProcessor<T, R extends Reward> {

    /**
     * 添加奖励
     *
     * @param target
     * @param reward
     * @param addKey
     * @return
     */
    void add(T target, R reward, String addKey);

    /**
     * 消费奖励
     *
     * @param target
     * @param reward
     * @param consumeKey
     * @return
     */
    void consume(T target, R reward, String consumeKey);

    boolean checkAdd(T target, R reward, String addKey);

    boolean checkConsume(T target, R reward, String consumeKey);

}
