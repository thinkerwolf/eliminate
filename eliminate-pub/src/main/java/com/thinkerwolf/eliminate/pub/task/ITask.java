package com.thinkerwolf.eliminate.pub.task;

import com.thinkerwolf.eliminate.pub.reward.RewardGroup;

import java.util.List;

/**
 * 一条任务
 *
 * @param <T> 任务的拥有者
 */
public interface ITask<T> {

    /**
     * 获取任务id
     *
     * @return
     */
    Object getTaskId();

    /**
     * 获取任务要求列表
     *
     * @return
     */
    List<ITaskRequest<T>> getRequests();

    /**
     * 获取任务静态信息
     *
     * @return
     */
    ITaskInfo getTaskInfo();

    /**
     * 开始任务
     */
    void startTask();

    /**
     * 停止任务
     */
    void stopTask();

    /**
     * 获取任务拥有者
     *
     * @return
     */
    T getEntity();

    /**
     * 当前进度String
     *
     * @return
     */
    String progressString();

    /**
     * 领取奖励
     *
     * @return
     */
    RewardGroup takeReward();
}
