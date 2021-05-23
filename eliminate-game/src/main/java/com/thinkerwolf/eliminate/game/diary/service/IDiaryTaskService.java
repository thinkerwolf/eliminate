package com.thinkerwolf.eliminate.game.diary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.game.diary.entity.PlayerTask;

import java.util.List;

public interface IDiaryTaskService extends IService<PlayerTask> {
    PlayerTask find(int playerId, int taskId);

    PlayerTask findOrCreate(int playerId, int taskId);

    List<PlayerTask> findList(int playerId);

    List<PlayerTask> getDayTasks(int playerId, int day);

    OpResult curTasks(int playerId);

    OpResult finish(int playerId, int taskId);

    OpResult takeReward(int playerId, int idx);
}
