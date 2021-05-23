package com.thinkerwolf.eliminate.pub.task;

import com.thinkerwolf.eliminate.pub.trigger.TriggerManager;

/**
 * @param <T>
 * @author wukai
 */
public abstract class AbstractTaskRequest<T> implements ITaskRequest<T> {


    @Override
    public int getTotalProgress() {
        return 1;
    }

    @Override
    public TaskRequestProgress<T> createProgress(T t) {
        return new TaskRequestProgress<>(t, this);
    }

    @Override
    public TaskRequestProgress<T> createProgress(T t, String progress) {
        return new TaskRequestProgress<>(t, this, progress);
    }


    protected void addTaskTrigger(T t, TaskTriggerListener<T> listener) {
        listener.getProgress().setTriggerListener(listener);
        TriggerManager.addTrigger(t, listener.getTriggerType(), listener);
    }

}
