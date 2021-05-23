package com.thinkerwolf.eliminate.pub.task;

import com.thinkerwolf.eliminate.pub.trigger.TriggerEvent;
import com.thinkerwolf.eliminate.pub.trigger.TriggerListener;
import com.thinkerwolf.eliminate.pub.trigger.TriggerType;

/**
 * @author wukai
 */
public abstract class TaskTriggerListener<T> implements TriggerListener {

    private final TriggerType triggerType;

    protected final TaskRequestProgress<T> progress;

    public TaskTriggerListener(TriggerType triggerType, TaskRequestProgress<T> progress) {
        this.triggerType = triggerType;
        this.progress = progress;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public TaskRequestProgress<T> getProgress() {
        return progress;
    }

    @Override
    public boolean handleEvent(TriggerEvent event) {
        boolean cp = progress.isComplete();
        if (cp) {
            return cp;
        }
        boolean notifyProcessChange = doHandleEvent(event);
        cp = progress.isComplete();
        if (cp) {
            progress.getProcessChangeListener().processChange();
        } else {
            if (notifyProcessChange) {
                progress.getProcessChangeListener().processChange();
            }
        }
        return cp;
    }

    protected abstract boolean doHandleEvent(TriggerEvent event);


}
