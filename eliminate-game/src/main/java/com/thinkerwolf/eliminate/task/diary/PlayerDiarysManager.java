package com.thinkerwolf.eliminate.task.diary;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.eliminate.pub.player.Players;

import java.util.Map;

/**
 * @author wukai
 * @since 2020-08-05
 */
public class PlayerDiarysManager {

    private static final Map<Integer, PlayerDiarys> playerDiaryMap = Maps.newConcurrentMap();

    public static void handleLogin(int playerId) {
        PlayerDiarys pds = playerDiaryMap.computeIfAbsent(playerId, PlayerDiarys::new);
        Map<String, Object> data = Maps.newHashMapWithExpectedSize(1);
        data.put("day", pds.getCurDay());
        Players.push(playerId, PushCommand.DIARY_DAY, data);
    }

    public static PlayerDiarys find(int playerId) {
        return playerDiaryMap.get(playerId);
    }

}
