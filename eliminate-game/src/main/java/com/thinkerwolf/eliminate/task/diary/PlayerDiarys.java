package com.thinkerwolf.eliminate.task.diary;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.diary.entity.PlayerDay;
import com.thinkerwolf.eliminate.diary.entity.PlayerTask;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.CharacterAction;
import com.thinkerwolf.eliminate.pub.sdata.entity.Talk;
import com.thinkerwolf.eliminate.pub.sdata.entity.TalkPop;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfo;
import com.thinkerwolf.eliminate.pub.task.diary.TaskInfoFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author wukai
 * @since 2020-08-05
 */
public class PlayerDiarys {
    /**
     * PlayerId
     */
    private final int playerId;
    /**
     * 玩家日记本当前所在的天数
     */
    private volatile int curDay;

    private final Map<Integer, PlayerDiary> diaryMap = new ConcurrentSkipListMap<>();

    public PlayerDiarys(int playerId) {
        this.playerId = playerId;
        init();
    }

    private void init() {
        List<PlayerTask> playerTasks = DataBusManager.getDataBus().getDiaryTaskService().findList(playerId);
        TreeMap<Integer, List<PlayerTask>> map = Maps.newTreeMap();
        playerTasks.forEach(pt -> {
            TaskInfo taskInfo = TaskInfoFactory.getTaskInfo(pt.getTaskId());
            List<PlayerTask> list = map.computeIfAbsent(taskInfo.getDay(), integer -> Lists.newLinkedList());
            list.add(pt);
        });
        Map.Entry<Integer, List<PlayerTask>> lastEntry = map.lastEntry();
        if (lastEntry != null) {
            this.curDay = lastEntry.getKey();
            for (PlayerTask pt : lastEntry.getValue()) {
                if (pt.getState() < DiaryTaskState.FINISH.getId()) {
                    diaryMap.computeIfAbsent(pt.getTaskId(), taskId -> new PlayerDiary(playerId, TaskInfoFactory.getTaskInfo(taskId)));
                }
            }
            if (checkDayOver()) {
                enterNextDay();
            }
        } else {
            List<TaskInfo> taskInfos = TaskInfoFactory.getFirstDayBeginTasks();
            this.curDay = taskInfos.get(0).getDay();
            for (TaskInfo taskInfo : taskInfos) {
                diaryMap.computeIfAbsent(taskInfo.getId(), taskId -> new PlayerDiary(playerId, taskInfo));
            }
        }
    }

    public int getCurDay() {
        return curDay;
    }

    public Map<Integer, PlayerDiary> getDiaryMap() {
        return diaryMap;
    }

    public int getPlayerId() {
        return playerId;
    }

    public OpResult addStar(int taskId, int star) {
        PlayerDiary pd = diaryMap.get(taskId);
        if (pd == null) {
            return OpResult.fail("任务不存在");
        }
        OpResult op = pd.addProgress(star);
        if (!op.isOk()) {
            return op;
        }
        if (pd.isComplete()) {
            List<TaskInfo> taskInfos = pd.getTaskInfo().getNexts();

            for (TaskInfo taskInfo : taskInfos) {
                diaryMap.computeIfAbsent(taskInfo.getId(), id -> new PlayerDiary(playerId, taskInfo));
            }
            // 加进度
            handleDayGift();
            // 弹出动作
            pushAction(playerId, pd.getTaskInfo().getTask().getCharacterActionGid());
        }
        return op;
    }

    private void handleDayGift() {
        PlayerDay playerDay = DataBusManager.getDataBus().getPlayerDayService().findOrCreate(playerId, curDay);
        int p = (int) Math.ceil(100.0D / TaskInfoFactory.getDayTasks(curDay).size());
        int np = Math.min(100, playerDay.getNextProcess() + p);
        playerDay.setNextProcess(np);
        DataBusManager.getDataBus().getPlayerDayService().saveOrUpdate(playerDay);
    }

    public static void pushAction(int playerId, Integer gid) {
        if (gid == null) {
            return;
        }
        List<CharacterAction> actions = SDataBusManager.getDataGetter().getCharacterActionCache().getGroup(gid);
        if (actions == null || actions.isEmpty()) {
            return;
        }
        final Map<String, Object> data = Maps.newLinkedHashMap();
        data.put("groupId", gid);
        List<Map<String, Object>> actionList = Lists.newLinkedList();
        for (CharacterAction action : actions) {
            Map<String, Object> m = Maps.newHashMapWithExpectedSize(1);

            if (action.getAction() != null) {
                m.put("point", action.getPoint());
                actionList.add(m);
            }

            if (action.getAction() != null) {
                m = Maps.newHashMapWithExpectedSize(1);
                m.put("action", action.getAction());
                actionList.add(m);
            }

            List<Talk> talks = SDataBusManager.getDataGetter().getTalkCache().getGroup(action.getTalkGid());
            if (talks != null && !talks.isEmpty()) {
                m = Maps.newHashMapWithExpectedSize(3);
                m.put("talkGid", action.getTalkGid());
                m.put("talkType", action.getTalkType());
                List<Map<String, Object>> tds = Lists.newLinkedList();
                for (Talk talk : talks) {
                    Map<String, Object> td = Maps.newLinkedHashMap();
                    td.put("seq", talk.getSeq());
                    td.put("rolePic", talk.getRolePic());
                    td.put("roleName", talk.getRoleName());
                    td.put("intro", talk.getIntro());
                    td.put("toward", talk.getToward());
                    tds.add(td);
                }
                m.put("ts", tds);
                actionList.add(m);
            }


            List<TalkPop> talkPops = SDataBusManager.getDataGetter().getTalkPopCache().getGroup(action.getTalkPopGid());
            if (talks != null && !talks.isEmpty()) {
                m = Maps.newHashMapWithExpectedSize(3);
                m.put("talkPopGid", action.getTalkGid());
                m.put("talkPopType", action.getTalkType());
                List<Map<String, Object>> tds = Lists.newLinkedList();
                for (TalkPop talkPop : talkPops) {
                    Map<String, Object> td = Maps.newHashMap();
                    td.put("seq", talkPop.getSeq());
                    td.put("intro", talkPop.getIntro());
                    tds.add(td);
                }
                m.put("ts", tds);
                actionList.add(m);
            }

        }
        data.put("actionList", actionList);
        Players.push(playerId, PushCommand.PLAYER_ACTION, data);
    }

    public boolean checkDayOver() {
        for (PlayerDiary pd : diaryMap.values()) {
            if (!pd.isComplete()) {
                return false;
            }
        }
        return true;
    }

    public void checkAndEnterNextDay() {
        if (checkDayOver()) {
            enterNextDay();
        }
    }

    private void enterNextDay() {
        List<TaskInfo> taskInfos = TaskInfoFactory.getNextDayBeginTasks(curDay);
        if (taskInfos == null || taskInfos.isEmpty()) {
            return;
        }
        this.diaryMap.clear();
        this.curDay = taskInfos.get(0).getDay();
        for (TaskInfo taskInfo : taskInfos) {
            this.diaryMap.computeIfAbsent(taskInfo.getId(), taskId -> new PlayerDiary(playerId, taskInfo));
        }
    }


    public void buildDoc(Map<String, Object> doc) {
        doc.put("day", curDay);
        List<Map<String, Object>> list = Lists.newLinkedList();
        diaryMap.forEach((taskId, pd) -> {
            Map<String, Object> m = Maps.newLinkedHashMap();
            pd.buildDoc(m);
            list.add(m);
        });
        doc.put("tasks", list);
    }

    public void clearFinish() {
        Set<Integer> set = Sets.newHashSet();
        diaryMap.forEach((taskId, pd) -> {
            if (pd.isComplete()) {
                set.add(taskId);
            }
        });
        set.forEach(diaryMap::remove);
    }

}
