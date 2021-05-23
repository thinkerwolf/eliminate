package com.thinkerwolf.eliminate.game.task.request;

import com.thinkerwolf.eliminate.pub.task.AbstractTaskRequest;
import com.thinkerwolf.eliminate.pub.task.TaskRequestProgress;
import com.thinkerwolf.eliminate.pub.task.TaskTriggerListener;
import com.thinkerwolf.eliminate.pub.trigger.TriggerEvent;
import com.thinkerwolf.eliminate.pub.trigger.TriggerType;

public class TaskRequestCost extends AbstractTaskRequest<Object> {

    private final int taskId;

    public TaskRequestCost(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void startRequest(Object obj, TaskRequestProgress<Object> progress) {
        addTaskTrigger(obj, new TaskTriggerListener<Object>(TriggerType.COST, progress) {
            @Override
            protected boolean doHandleEvent(TriggerEvent event) {
                if (event.getParams().length < 1) {
                    return false;
                }
                if (event.getSource() == obj && event.getParams()[0].equals(taskId)) {
                    progress.incrementCurrentCount();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void finishDirect() {

    }
}
