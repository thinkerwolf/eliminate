package com.thinkerwolf.eliminate.game.diary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.util.GameUtil;
import com.thinkerwolf.eliminate.game.databus.DataBusManager;
import com.thinkerwolf.eliminate.game.diary.comm.DiaryTaskUtil;
import com.thinkerwolf.eliminate.game.diary.entity.PlayerDay;
import com.thinkerwolf.eliminate.game.diary.entity.PlayerTask;
import com.thinkerwolf.eliminate.game.diary.mapper.PlayerTaskMapper;
import com.thinkerwolf.eliminate.game.diary.service.IDiaryTaskService;
import com.thinkerwolf.eliminate.pub.reward.RewardGroup;
import com.thinkerwolf.eliminate.pub.reward.RewardKey;
import com.thinkerwolf.eliminate.pub.reward.RewardManager;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardStar;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.DayGift;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfoFactory;
import com.thinkerwolf.eliminate.game.task.diary.DiaryTaskState;
import com.thinkerwolf.eliminate.game.task.diary.PlayerDiarys;
import com.thinkerwolf.eliminate.game.task.diary.PlayerDiarysManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("diaryTaskService")
public class DiaryTaskServiceImpl extends ServiceImpl<PlayerTaskMapper, PlayerTask> implements IDiaryTaskService {

    @Override
    public PlayerTask find(int playerId, int taskId) {
        QueryWrapper<PlayerTask> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId).eq("task_id", taskId);
        return getOne(qw);
    }

    @Override
    public PlayerTask findOrCreate(int playerId, int taskId) {
        QueryWrapper<PlayerTask> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId).eq("task_id", taskId);
        PlayerTask playerTask = getOne(qw);
        if (playerTask == null) {
            playerTask = new PlayerTask();
            playerTask.setPlayerId(playerId);
            playerTask.setProcess("");
            playerTask.setState(DiaryTaskState.NONE.getId());
            playerTask.setTaskId(taskId);
            saveOrUpdate(playerTask);
        }
        return playerTask;
    }

    @Override
    public List<PlayerTask> findList(int playerId) {
        QueryWrapper<PlayerTask> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId);
        return list(qw);
    }

    @Override
    public List<PlayerTask> getDayTasks(int playerId, int day) {
        List<PlayerTask> tasks = findList(playerId);
        List<PlayerTask> res = Lists.newLinkedList();
        for (PlayerTask pt : tasks) {
            if (TaskInfoFactory.getTaskInfo(pt.getTaskId()).getTask().getDay().equals(day)) {
                res.add(pt);
            }
        }
        return res;
    }

    @Override
    public OpResult curTasks(int playerId) {
        Map<String, Object> doc = Maps.newHashMap();

        PlayerDiarys pdi = PlayerDiarysManager.find(playerId);

        // ??????
        Map<String, Object> processDoc = Maps.newHashMap();
        PlayerDay playerDay = DataBusManager.getDataBus().getPlayerDayService().findOrCreate(playerId, pdi.getCurDay());
        processDoc.put("cp", playerDay.getCurrentProcess());
        processDoc.put("np", playerDay.getNextProcess());

        List<Map<String, Object>> dgsDoc = Lists.newLinkedList();
        List<DayGift> dayGifts = SDataBusManager.getDataGetter().getDayGiftCache().getDayGifts(pdi.getCurDay());
        for (DayGift dayGift : dayGifts) {
            Map<String, Object> m = Maps.newHashMapWithExpectedSize(3);
            m.put("p", dayGift.getProgress());
            m.put("pic", dayGift.getPic());
            if (DiaryTaskUtil.isAutoTask(playerDay.getCurrentProcess(), playerDay.getNextProcess(), dayGift)) {
                RewardGroup group = RewardGroup.group(dayGift.getReward());
                RewardManager.addRewardGroup(playerId, group, RewardKey.DIARY_TASK_REWARD);
                m.put("rewards", group.getRewards());
            }
            dgsDoc.add(m);
        }

        processDoc.put("dgs", dgsDoc);

        playerDay.setCurrentProcess(playerDay.getNextProcess());
        DataBusManager.getDataBus().getPlayerDayService().saveOrUpdate(playerDay);

        // ????????????
        Map<String, Object> taskDoc = Maps.newHashMap();
        pdi.buildDoc(taskDoc);
        pdi.clearFinish();

        doc.put("ps", processDoc);
        doc.put("ts", taskDoc);


        // ?????????????????????????????????????????????
        if (playerDay.getNextProcess() >= 100) {
            pdi.checkAndEnterNextDay();
        }

        return OpResult.ok(doc);
    }

    @Override
    public OpResult finish(int playerId, int taskId) {
        PlayerDiarys pdi = PlayerDiarysManager.find(playerId);
        RewardStar rewardStar = new RewardStar(1);
        OpResult op = RewardManager.checkConsumeReward(playerId, rewardStar, RewardKey.DIARY_TASK_STAR);
//        if (!op.isOk()) {
//            return OpResult.fail(LocalMessages.T_DIARY_TASK_4);
//        }
        return pdi.addStar(taskId, 1);
    }

    @Override
    public OpResult takeReward(int playerId, int idx) {
        PlayerDiarys pdi = PlayerDiarysManager.find(playerId);
        List<DayGift> dayGifts = SDataBusManager.getDataGetter().getDayGiftCache().getDayGifts(pdi.getCurDay());
        if (idx - 1 >= dayGifts.size()) {
            return OpResult.fail(LocalMessages.T_DIARY_TASK_5);
        }
        PlayerDay playerDay = DataBusManager.getDataBus().getPlayerDayService().findOrCreate(playerId, pdi.getCurDay());
        List<Integer> takes = GameUtil.stringToInts(playerDay.getTakes());
        if (takes.contains(idx)) {
            return OpResult.fail(LocalMessages.T_DIARY_TASK_6);
        }
        DayGift dayGift = dayGifts.get(idx - 1);
        if (playerDay.getNextProcess() < dayGift.getProgress()) {
            return OpResult.fail(LocalMessages.T_DIARY_TASK_5);
        }
        RewardGroup group = RewardGroup.group(dayGift.getReward());
        takes.add(idx);
        playerDay.setTakes(GameUtil.listToString(takes));
        DataBusManager.getDataBus().getPlayerDayService().saveOrUpdate(playerDay);
        RewardManager.addRewardGroup(playerId, group, RewardKey.DIARY_TASK_REWARD);

        // ???????????????????????????
        if (takes.size() >= dayGifts.size()) {
            pdi.checkAndEnterNextDay();
        }

        return OpResult.ok(group);
    }


}
