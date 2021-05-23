package com.thinkerwolf.eliminate.diary.service;

import com.thinkerwolf.eliminate.diary.entity.PlayerTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.task.diary.PlayerDiaryTask;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wukai
 * @since 2020-06-05
 */
public interface IPlayerDiaryTaskService extends IService<PlayerTask> {

    PlayerTask getTask(int playerId, int taskId);

    PlayerTask getTaskSafty(int playerId, int taskId);

    List<PlayerTask> getTasks(int playerId);

    void updateTask(PlayerDiaryTask task);

    /**
     * 获取当天的日记本任务
     *
     * @param playerId
     * @return
     */
    OpResult getDayTasks(int playerId);

}
