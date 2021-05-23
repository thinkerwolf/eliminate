package com.thinkerwolf.eliminate.pub.task;

import com.thinkerwolf.eliminate.pub.reward.RewardGroup;

import java.util.List;

/**
 * 一条任务的静态信息
 *
 * @author wukai
 */
public interface ITaskInfo {
    /**
     * 任务id
     *
     * @return
     */
    Object getId();

    /**
     * 任务名称
     *
     * @return
     */
    String getName();

    List<ITaskRequest> getRequests();

    default RewardGroup getRewardGroup() {
        return null;
    }

}
