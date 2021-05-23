package com.thinkerwolf.eliminate.game.task.request;

import com.thinkerwolf.eliminate.game.databus.DataBusManager;
import com.thinkerwolf.eliminate.pub.task.AbstractTaskRequest;
import com.thinkerwolf.eliminate.pub.task.TaskRequestProgress;
import com.thinkerwolf.eliminate.pub.task.TaskTriggerListener;
import com.thinkerwolf.eliminate.pub.trigger.TriggerEvent;
import com.thinkerwolf.eliminate.pub.trigger.TriggerType;
import com.thinkerwolf.eliminate.game.stage.entity.PlayerStage;

public class TaskRequestStage extends AbstractTaskRequest<Integer> {

    private Integer stageId;

    public TaskRequestStage(Integer stageId) {
        this.stageId = stageId;
    }

    @Override
    public void startRequest(Integer playerId, TaskRequestProgress<Integer> progress) {
        addTaskTrigger(playerId, new TaskTriggerListener<Integer>(TriggerType.STAGE, progress) {
            @Override
            protected boolean doHandleEvent(TriggerEvent event) {
                PlayerStage ps = DataBusManager.getDataBus().getPlayerStageService().getStage(playerId, stageId);
                if (ps != null && ps.getState() == 2) {
                    progress.setCurrentCount(1);
                    return true;
                }
                return false;
            }
        });
    }
}
