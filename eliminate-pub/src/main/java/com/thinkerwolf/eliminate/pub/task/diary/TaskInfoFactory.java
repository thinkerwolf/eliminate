package com.thinkerwolf.eliminate.pub.task.diary;

import com.google.common.collect.*;
import com.thinkerwolf.eliminate.common.util.GameUtil;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.Task;

import java.util.*;

public class TaskInfoFactory {

    private static final Map<Integer, TaskInfo> idTaskInfos = Maps.newHashMap();

    private static final Map<Integer, List<TaskInfo>> dayTaskInfos = Maps.newHashMap();

    private static final NavigableMap<Integer, List<TaskInfo>> dayBeginTaskInfos = Maps.newTreeMap();

    public static void init() {
        idTaskInfos.clear();
        dayTaskInfos.clear();
        dayBeginTaskInfos.clear();
        List<Task> tasks = SDataBusManager.getDataGetter().getTaskCache().getAll();
        for (Task task : tasks) {
            TaskInfo d = new TaskInfo(task);
            idTaskInfos.put(task.getId(), d);
            if (task.getBegin() == 1) {
                List<TaskInfo> list = dayBeginTaskInfos.computeIfAbsent(task.getDay(), (t) -> new ArrayList<>());
                list.add(d);
            }
        }

        if (dayBeginTaskInfos.isEmpty()) {
            throw new IllegalArgumentException("Begin task is empty");
        }

        for (Task task : tasks) {
            TaskInfo curInfo = idTaskInfos.get(task.getId());
            List<TaskInfo> list = dayTaskInfos.computeIfAbsent(task.getDay(), k -> Lists.newArrayList());
            list.add(curInfo);
            curInfo.nexts = Lists.newArrayList();
            for (Integer next : GameUtil.stringToInts(task.getNext())) {
                TaskInfo nextInfo = idTaskInfos.get(next);
                if (nextInfo != null) {
                    nextInfo.previous = curInfo;
                    curInfo.nexts.add(nextInfo);
                    if (!nextInfo.getTask().getDay().equals(curInfo.getTask().getDay())) {
                        throw new IllegalArgumentException("Task[" + curInfo.getId() + "] and NextTask[" + nextInfo.getId() + "] are not the same day");
                    }
                }
            }
        }
    }

    /**
     * 获取第一天开始任务列表
     *
     * @return
     */
    public static List<TaskInfo> getFirstDayBeginTasks() {
        return dayBeginTaskInfos.firstEntry().getValue();
    }

    /**
     * 获取某一天开始任务列表
     *
     * @param day
     * @return
     */
    public static List<TaskInfo> getDayBeginTasks(int day) {
        return dayBeginTaskInfos.get(day);
    }

    /**
     * 获取后一天开始任务列表
     *
     * @param day
     * @return
     */
    public static List<TaskInfo> getNextDayBeginTasks(int day) {
        Map.Entry<Integer, List<TaskInfo>> en = dayBeginTaskInfos.higherEntry(day);
        if (en == null) {
            return null;
        }
        return en.getValue();
    }

    /**
     * @param taskId
     * @return
     */
    public static TaskInfo getTaskInfo(int taskId) {
        return idTaskInfos.get(taskId);
    }

    public static List<TaskInfo> getDayTasks(int day) {
        return dayTaskInfos.get(day);
    }
}
