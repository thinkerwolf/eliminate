package com.thinkerwolf.eliminate.common;

/**
 * 推送command
 *
 * @author wukai
 * @since 2020-05-26
 */
public enum PushCommand {

    /**
     * 玩家下线
     */
    PLAYER_OFFLINE("push@player", "offline"),
    /**
     * 玩家体力
     */
    PLAYER_VIT("push@player", "vit"),
    /**
     * 玩家星星
     */
    PLAYER_STAR("push@player", "star"),
    /**
     * 玩家行为动作
     */
    PLAYER_ACTION("push@player", "action"),
    /**
     * 建筑状态
     */
    BUILDING_INFO("push@building", "info"),
    /**
     * 所有建筑状态信息
     */
    BUILDING_ALL_INFO("push@building", "all"),
    /**
     * 一条日记本任务状态
     */
    DIARY_TASK_INFO("push@diary", "info"),

    DIARY_DAY("push@diary", "day"),

    /**
     * 当前战斗信息
     */
    BATTLE_INFO("push@battle", "info"),
    /**
     * 战斗初始化信息
     */
    BATTLE_INI("push@battle", "ini"),
    /**
     * 从地图上取块的推送
     */
    BATTLE_TAKE_BLOCK("push@battle", "takeBlock"),
    /**
     * 一个块回到地图上
     */
    BATTLE_BACK_BLOCK("push@battle", "backBlock"),
    /**
     * 随机重排地图块
     */
    BATTLE_SHUFFLE_BLOCK("push@battle", "shuffleBlock"),
    /**
     * 战斗结束
     */
    BATTLE_END("push@battle", "end"),

    CHAT_RECEIVE("push@chat", "receive"),

    ;

    private final String command;

    private final String module;

    PushCommand(String command, String module) {
        this.command = command;
        this.module = module;
    }

    public String getCommand() {
        return command;
    }

    public String getModule() {
        return module;
    }
}
