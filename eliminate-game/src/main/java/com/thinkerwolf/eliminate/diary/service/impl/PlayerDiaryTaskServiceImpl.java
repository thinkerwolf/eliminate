package com.thinkerwolf.eliminate.diary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.diary.entity.PlayerTask;
import com.thinkerwolf.eliminate.diary.mapper.PlayerTaskMapper;
import com.thinkerwolf.eliminate.diary.service.IPlayerDiaryTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.pub.reward.RewardKey;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfo;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfoFactory;
import com.thinkerwolf.eliminate.pub.trigger.TriggerEvent;
import com.thinkerwolf.eliminate.pub.trigger.TriggerManager;
import com.thinkerwolf.eliminate.pub.trigger.TriggerType;
import com.thinkerwolf.eliminate.task.diary.DiaryTaskState;
import com.thinkerwolf.eliminate.task.diary.PlayerDiaryTask;
import com.thinkerwolf.eliminate.task.diary.PlayerDiaryTaskManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-06-05
 */
@Service("playerTaskService")
public class PlayerDiaryTaskServiceImpl extends ServiceImpl<PlayerTaskMapper, PlayerTask> implements IPlayerDiaryTaskService {


    @Override
    public PlayerTask getTask(int playerId, int taskId) {
        QueryWrapper<PlayerTask> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId).eq("task_id", taskId);
        return getOne(qw);
    }

    @Override
    public PlayerTask getTaskSafty(int playerId, int taskId) {
        QueryWrapper<PlayerTask> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId).eq("task_id", taskId);
        PlayerTask playerTask = getOne(qw);
        if (playerTask == null) {
            playerTask = new PlayerTask();
            playerTask.setPlayerId(playerId);
            playerTask.setProcess("");
            playerTask.setState(DiaryTaskState.NONE.getId());
            playerTask.setTaskId(taskId);
            saveOrUpdate(playerTask);
        }
        return playerTask;
    }

    @Override
    public List<PlayerTask> getTasks(int playerId) {
        QueryWrapper<PlayerTask> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId);
        return list(qw);
    }

    @Override
    public void updateTask(PlayerDiaryTask task) {
        PlayerTask playerTask = getTaskSafty(task.getEntity(), task.getTaskId());
        playerTask.setProcess(task.progressString());
        playerTask.setState(task.getState());
        saveOrUpdate(playerTask);
    }

    @Override
    public OpResult getDayTasks(int playerId) {
        PlayerDiaryTask curPd = PlayerDiaryTaskManager.getInstance().getCurTask(playerId);
        if (curPd == null) {
            return OpResult.fail(LocalMessages.T_DIARY_TASK_3);
        }
        List<TaskInfo> taskInfos = TaskInfoFactory.getDayTasks(curPd.getTaskInfo().getTask().getDay());
        Map<String, Object> map = Maps.newHashMap();
        List<Map<String, Object>> list = Lists.newLinkedList();
        for (TaskInfo taskInfo : taskInfos) {
            Map<String, Object> m = Maps.newHashMapWithExpectedSize(10);
            PlayerDiaryTask pd = PlayerDiaryTaskManager.getInstance().getTask(playerId, taskInfo.getId());
            if (pd != null) {
                pd.buildDoc(m);
            } else {
                taskInfo.buildDoc(m);
                m.put("state", DiaryTaskState.NONE.getId());
            }
            m.put("intro", taskInfo.getTask().getIntro());
            list.add(m);
        }
        map.put("tasks", list);
        return OpResult.ok(map);
    }
}
