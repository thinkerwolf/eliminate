package com.thinkerwolf.eliminate.pub.task;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractTask<T> implements ITask<T>, ITaskProgressChangeListener {

    private static final AtomicIntegerFieldUpdater<AbstractTask> stateUpdater
            = AtomicIntegerFieldUpdater.newUpdater(AbstractTask.class, "state");

    private final T entity;
    private final List<ITaskRequest<T>> requests;
    private List<TaskRequestProgress> progresses;
    /**
     * 任务状态
     */
    private volatile int state;


    public AbstractTask(T entity, List<ITaskRequest<T>> requests) {
        this.entity = entity;
        this.requests = requests;
    }

//    public AbstractTask(T entity, List<ITaskRequest<T>> requests, String progress) {
//        this.entity = entity;
//        this.requests = requests;
//        this.progress = progress;
//    }

    public void setState(int state) {
        stateUpdater.set(this, state);
    }

    public int getState() {
        return state;
    }

    public T getEntity() {
        return entity;
    }

    @Override
    public void stopTask() {
        for (TaskRequestProgress progress : progresses) {
            progress.stopProgress();
        }
    }

    @Override
    public void processChange() {
        doProcessChange();
    }

    @Override
    public List<ITaskRequest<T>> getRequests() {
        return requests;
    }

    @Override
    public String progressString() {
        StringBuilder sb = new StringBuilder();
        for (TaskRequestProgress progress : progresses) {
            sb.append(progress.toString()).append(';');
        }
        return sb.toString();
    }

    /**
     * 要求是否都完成
     *
     * @return bool
     */
    protected boolean requestComplete() {
        for (TaskRequestProgress progress : progresses) {
            if (!progress.isComplete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取任务进度，Maybe null
     *
     * @return list
     */
    protected List<TaskRequestProgress> getProgresses() {
        return progresses;
    }

    protected void doStartTask() {
        this.progresses = Lists.newArrayList();
        String[] ss = StringUtils.split(getProgressString(), ';');
        int idx = 0;
        for (ITaskRequest<T> request : requests) {
            String progressStr = ss == null || ss.length <= idx ? null : ss[idx];
            TaskRequestProgress progress;
            if (StringUtils.isBlank(progressStr)) {
                progress = request.createProgress(entity);
            } else {
                progress = request.createProgress(entity, progressStr);
            }
            progress.setProcessChangeListener(this);
            progresses.add(progress);
            idx++;
        }
        for (int i = 0; i < requests.size(); i++) {
            requests.get(i).startRequest(entity, progresses.get(i));
        }
    }

    protected abstract void doProcessChange();

    protected abstract String getProgressString();

    /**
     * 将任务信息更新到数据库中
     */
    protected abstract void saveTask();

    /**
     * 任务完成后是否自动领奖
     *
     * @return bool
     */
    protected abstract boolean isAutoTakeReward();

}
