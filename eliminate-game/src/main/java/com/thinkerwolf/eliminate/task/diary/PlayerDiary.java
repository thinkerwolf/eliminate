package com.thinkerwolf.eliminate.task.diary;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.diary.entity.PlayerTask;
import com.thinkerwolf.eliminate.pub.reward.RewardKey;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfo;
import com.thinkerwolf.gamer.common.SymbolConstants;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class PlayerDiary {

    private final int playerId;
    private final TaskInfo taskInfo;
    private int currentProgress;

    public PlayerDiary(int playerId, TaskInfo taskInfo) {
        this.playerId = playerId;
        this.taskInfo = taskInfo;
        PlayerTask pt = DataBusManager.getDataBus().getDiaryTaskService().findOrCreate(playerId, taskInfo.getId());
        if (StringUtils.isBlank(pt.getProcess())) {
            pt.setState(DiaryTaskState.START.getId());
            pt.setProcess(String.valueOf(currentProgress));
            DataBusManager.getDataBus().getDiaryTaskService().saveOrUpdate(pt);
        } else {
            this.currentProgress = Integer.parseInt(pt.getProcess());
        }
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isComplete() {
        return currentProgress >= taskInfo.getCost();
    }

    public String createProcess() {
        return String.valueOf(currentProgress);
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public OpResult addProgress(int num) {
        if (isComplete()) {
            return OpResult.fail("已经完成");
        }
        this.currentProgress = Math.min(taskInfo.getCost(), currentProgress + num);
        PlayerTask pt = DataBusManager.getDataBus().getDiaryTaskService().findOrCreate(playerId, taskInfo.getId());
        pt.setProcess(createProcess());
        if (isComplete()) {
            RewardManager.addRewardGroup(playerId, taskInfo.getRewardGroup(), RewardKey.DIARY_TASK_REWARD);
            pt.setState(DiaryTaskState.REWARD.getId());
        }
        DataBusManager.getDataBus().getDiaryTaskService().saveOrUpdate(pt);
        return OpResult.ok();
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void buildDoc(Map<String, Object> doc) {
        PlayerTask pt = DataBusManager.getDataBus().getDiaryTaskService().findOrCreate(playerId, taskInfo.getId());
        doc.put("taskId", taskInfo.getId());
        doc.put("state", pt.getState());
        doc.put("pic", taskInfo.getPic());
        doc.put("name", taskInfo.getName());
        doc.put("intro", taskInfo.getTask().getIntro());
        doc.put("cc", getCurrentProgress());
        doc.put("tc", taskInfo.getCost());

    }

}
