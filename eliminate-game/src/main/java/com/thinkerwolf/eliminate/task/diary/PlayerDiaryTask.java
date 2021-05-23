package com.thinkerwolf.eliminate.task.diary;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.diary.entity.PlayerTask;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.reward.RewardGroup;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.task.AbstractTask;
import com.thinkerwolf.eliminate.pub.task.TaskRequestProgress;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfo;

import java.util.List;
import java.util.Map;

/**
 * 日记本一条任务
 *
 * @author wukai
 * @since 2020-06-04 23:00
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PlayerDiaryTask extends AbstractTask {

    private final TaskInfo taskInfo;

    public PlayerDiaryTask(Integer entity, TaskInfo taskInfo) {
        super(entity, taskInfo.getRequests());
        this.taskInfo = taskInfo;
        PlayerTask playerTask = DataBusManager.getDataBus().getPlayerTaskService().getTaskSafty(getEntity(), taskInfo.getId());
        setState(DiaryTaskState.idOf(playerTask.getState()).getId());
    }

    @Override
    public Integer getEntity() {
        return (Integer) super.getEntity();
    }

    @Override
    public RewardGroup takeReward() {
        if (taskInfo.getRewardGroup() != null) {
            RewardManager.addRewardGroup(getEntity(), taskInfo.getRewardGroup(), "diaryTask");
        }
        setState(DiaryTaskState.REWARD.getId());
        saveTask();
        pushInfo();
        return taskInfo.getRewardGroup();
    }

    @Override
    protected void doProcessChange() {
        if (requestComplete()) {
            if (isAutoTakeReward()) {
                takeReward();
            } else {
                setState(DiaryTaskState.FINISH.getId());
                saveTask();
            }
            doAfterFinish();
        }
    }

    private void doAfterFinish() {
        if (taskInfo.getNexts() != null) {
            for (TaskInfo next : taskInfo.getNexts()) {
                PlayerDiaryTask pd = PlayerDiaryTaskManager.getInstance().addTask(getEntity(), next.getId());
                pd.startTask();
                pd.pushInfo();
            }
        }
    }

    @Override
    public void startTask() {
        int state = getState();
        if (state <= DiaryTaskState.START.getId()) {
            doStartTask();
            setState(DiaryTaskState.START.getId());
            saveTask();
            processChange();
        }
    }

    @Override
    protected String getProgressString() {
        PlayerTask playerTask = DataBusManager.getDataBus().getPlayerTaskService().getTaskSafty(getEntity(), taskInfo.getId());
        return playerTask.getProcess();
    }

    @Override
    protected void saveTask() {
        DataBusManager.getDataBus().getPlayerTaskService().updateTask(this);
    }

    @Override
    protected boolean isAutoTakeReward() {
        return true;
    }

    @Override
    public Integer getTaskId() {
        return taskInfo.getId();
    }

    @Override
    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public int getCurStageId() {
        int curId = -1;
        List<TaskRequestProgress<?>> progs = getProgresses();
        if (progs == null) {
            return 0;
        }
        for (int i = 0; i < progs.size(); i++) {
            if (!progs.get(i).isComplete()) {
                curId = i;
                break;
            }
        }
        return curId < 0 ? 0 : taskInfo.getStageIds().get(curId);
    }

    public int getLeftStages() {
        // 获得剩余关卡数
        List<TaskRequestProgress> progs = getProgresses();
        if (progs == null) {
            return 0;
        }
        int leftNum = progs.size();
        for (TaskRequestProgress prog : progs) {
            if (prog.isComplete()) {
                leftNum--;
            }
        }
        return leftNum;
    }

    public void buildDoc(Map<String, Object> doc) {
        taskInfo.buildDoc(doc);
//        doc.put("stageId", getCurStageId());
//        doc.put("left", getLeftStages());
        doc.put("state", getState());
        List<TaskRequestProgress> progresses = getProgresses();
        doc.put("tc", progresses.get(0).getTotalCount());
        doc.put("cc", progresses.get(0).getCurrentCount());
    }

    public void pushInfo() {
        Map<String, Object> doc = Maps.newHashMapWithExpectedSize(5);
        buildDoc(doc);
        Players.push(getEntity(), PushCommand.DIARY_TASK_INFO, doc);
    }

}
