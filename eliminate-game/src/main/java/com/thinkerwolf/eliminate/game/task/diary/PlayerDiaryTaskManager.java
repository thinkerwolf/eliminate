package com.thinkerwolf.eliminate.game.task.diary;

import com.google.common.collect.*;
import com.thinkerwolf.eliminate.game.databus.DataBusManager;
import com.thinkerwolf.eliminate.game.diary.entity.PlayerTask;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfo;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfoFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 玩家日记本任务管理器
 *
 * @author wukai
 * @since 2020-06-04 23:14
 */
public class PlayerDiaryTaskManager {

    private PlayerDiaryTaskManager() {
    }

    private static final PlayerDiaryTaskManager INSTANCE = new PlayerDiaryTaskManager();

    public static PlayerDiaryTaskManager getInstance() {
        return INSTANCE;
    }

    private final Map<Integer, Map<Integer, PlayerDiaryTask>> playerDiaryTasks = Maps.newConcurrentMap();

    public void handleLogin(int playerId) {
        playerDiaryTasks.computeIfAbsent(playerId, k -> {
            Map<Integer, PlayerDiaryTask> map = new ConcurrentSkipListMap<>();
            List<PlayerTask> tasks = DataBusManager.getDataBus().getPlayerTaskService().getTasks(playerId);
            if (tasks == null || tasks.size() == 0) {
                List<TaskInfo> taskInfos = TaskInfoFactory.getFirstDayBeginTasks();
                for (TaskInfo taskInfo : taskInfos) {
                    PlayerDiaryTask pd = new PlayerDiaryTask(playerId, taskInfo);
                    map.put(taskInfo.getId(), pd);
                    pd.startTask();
                }
            } else {
                for (PlayerTask task : tasks) {
                    TaskInfo taskInfo = TaskInfoFactory.getTaskInfo(task.getTaskId());
                    PlayerDiaryTask pd = new PlayerDiaryTask(playerId, taskInfo);
                    map.put(task.getTaskId(), pd);
                    pd.startTask();
                }
            }
            return map;
        });
    }

    public PlayerDiaryTask getTask(int playerId, int taskId) {
        Map<Integer, PlayerDiaryTask> map = playerDiaryTasks.get(playerId);
        return map == null ? null : map.get(taskId);
    }

    public PlayerDiaryTask addTask(int playerId, int taskId) {
        Map<Integer, PlayerDiaryTask> map = playerDiaryTasks.get(playerId);
        TaskInfo taskInfo = TaskInfoFactory.getTaskInfo(taskId);
        PlayerDiaryTask pd = new PlayerDiaryTask(playerId, taskInfo);
        map.put(taskId, pd);
        return pd;
    }

    public PlayerDiaryTask getCurTask(int playerId) {
        Map<Integer, PlayerDiaryTask> map = playerDiaryTasks.get(playerId);
        if (map == null) {
            return null;
        }
        for (PlayerDiaryTask pd : map.values()) {
            if (pd.getState() < DiaryTaskState.FINISH.getId()) {
                return pd;
            }
        }
        return null;
    }


}
