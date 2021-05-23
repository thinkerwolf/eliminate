package com.thinkerwolf.eliminate.pub.task.diary;

import com.google.common.collect.Lists;
import com.thinkerwolf.eliminate.common.ScriptFactory;
import com.thinkerwolf.eliminate.pub.reward.RewardGroup;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardStar;
import com.thinkerwolf.eliminate.pub.sdata.entity.Task;
import com.thinkerwolf.eliminate.pub.task.ITaskInfo;
import com.thinkerwolf.eliminate.pub.task.ITaskRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.List;
import java.util.Map;

public class TaskInfo implements ITaskInfo {

    private Task task;

    private List<ITaskRequest> requests;
    /**
     * 上一条任务信息
     */
    TaskInfo previous;

    List<TaskInfo> nexts;

    private RewardGroup rewardGroup;


    private List<Integer> stageIds;

    public TaskInfo(Task task) {
        this.task = task;
        this.stageIds = Lists.newArrayList();
        this.requests = ScriptFactory.buildList("cost:" + task.getId(), ITaskRequest.class);
        this.rewardGroup = RewardGroup.group(task.getReward());
    }

    public Task getTask() {
        return task;
    }

    @Override
    public Integer getId() {
        return task.getId();
    }

    @Override
    public String getName() {
        return task.getKeyword();
    }

    @Override
    public List<ITaskRequest> getRequests() {
        return requests;
    }

    @Override
    public RewardGroup getRewardGroup() {
        return rewardGroup;
    }

    public int getDay() {
        return task.getDay();
    }

    public int getCost() {
        return task.getCost();
    }

    public String getPic() {
        return task.getPic();
    }

    /**
     * 获取后面的任务
     *
     * @return
     */
    public List<TaskInfo> getNexts() {
        return nexts;
    }

    /**
     * 获取上一条任务信息
     *
     * @return may be null
     */
    public TaskInfo getPrevious() {
        return previous;
    }

    public List<Integer> getStageIds() {
        return stageIds;
    }

    public void buildDoc(Map<String, Object> doc) {
        doc.put("taskId", getId());
        doc.put("day", task.getDay());
        doc.put("time", task.getTime());
        doc.put("kw", task.getKeyword());
        doc.put("pic", task.getPic());
    }
}
