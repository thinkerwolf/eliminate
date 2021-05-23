package com.thinkerwolf.eliminate.pub.task;

/**
 * 任务要求
 *
 * @author wukai
 */
public interface ITaskRequest<T> {
    /**
     * 开始任务要求
     *
     * @param t
     */
    TaskRequestProgress<T> createProgress(T t);

    /**
     * 开始任务要求
     *
     * @param t
     * @param progress 已经完成的进度
     */
    TaskRequestProgress<T> createProgress(T t, String progress);

    /**
     * 开始任务要求
     *
     * @param t
     */
    void startRequest(T t, TaskRequestProgress<T> progress);


    /**
     * 任务总进度
     *
     * @return
     */
    int getTotalProgress();

    /**
     * 任务直接完成，GM使用
     */
    default void finishDirect() {

    }

}
