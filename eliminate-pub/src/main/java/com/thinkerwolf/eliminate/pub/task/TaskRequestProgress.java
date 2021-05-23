package com.thinkerwolf.eliminate.pub.task;

import com.thinkerwolf.eliminate.pub.trigger.TriggerManager;
import com.thinkerwolf.gamer.common.Constants;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 任务进度
 *
 * @param <T>
 * @author wukai
 */
public class TaskRequestProgress<T> {

    private static final AtomicIntegerFieldUpdater<TaskRequestProgress> updater
            = AtomicIntegerFieldUpdater.newUpdater(TaskRequestProgress.class, "currentCount");

    private T target;

    private ITaskRequest<T> request;

    private final int totalCount;

    private volatile int currentCount;

    private ITaskProgressChangeListener processChangeListener;

    private TaskTriggerListener<T> triggerListener;

    public TaskRequestProgress(T target, ITaskRequest<T> request) {
        this.target = target;
        this.request = request;
        this.totalCount = request.getTotalProgress();
    }

    public TaskRequestProgress(T target, ITaskRequest<T> request, String progress) {
        String[] num = Constants.COLON_SPLIT_PATTERN.split(progress);
        this.target = target;
        this.request = request;
        this.currentCount = Integer.parseInt(num[0]);
        this.totalCount = Integer.parseInt(num[1]);
    }

    public boolean isComplete() {
        return getCurrentCount() >= getTotalCount();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setCurrentCount(int currentCount) {
        updater.set(this, currentCount);
    }

    public int getCurrentCount() {
        return updater.get(this);
    }

    public ITaskProgressChangeListener getProcessChangeListener() {
        return processChangeListener;
    }

    public void setProcessChangeListener(ITaskProgressChangeListener processChangeListener) {
        this.processChangeListener = processChangeListener;
    }

    public TaskTriggerListener<T> getTriggerListener() {
        return triggerListener;
    }

    public void setTriggerListener(TaskTriggerListener<T> triggerListener) {
        this.triggerListener = triggerListener;
    }

    @Override
    public String toString() {
        return currentCount + ":" + totalCount;
    }

    public void incrementCurrentCount() {
        updater.getAndUpdate(this, operand -> {
            if (operand >= totalCount) {
                operand = totalCount;
            } else {
                operand++;
            }
            return operand;
        });
    }

    /**
     * 停止进度
     *
     * @author wukai
     */
    public void stopProgress() {
        TriggerManager.removeTrigger(target, triggerListener.getTriggerType(), triggerListener);
    }
}
