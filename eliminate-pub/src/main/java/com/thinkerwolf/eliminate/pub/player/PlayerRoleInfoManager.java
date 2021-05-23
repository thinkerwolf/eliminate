package com.thinkerwolf.eliminate.pub.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRoleInfoManager {

    private static final PlayerRoleInfoManager INSTANCE = new PlayerRoleInfoManager();

    private final Map<Integer, PlayerRoleInfo> roleInfoMap = new ConcurrentHashMap<>();

    public static PlayerRoleInfoManager getInstance() {
        return INSTANCE;
    }

    private PlayerRoleInfoManager(){}

    public PlayerRoleInfo getRole(int playerId) {
        return roleInfoMap.get(playerId);
    }

    public PlayerRoleInfo setRole(PlayerRoleInfo playerRoleInfo) {
        return roleInfoMap.putIfAbsent(playerRoleInfo.getPlayerId(), playerRoleInfo);
    }
}
