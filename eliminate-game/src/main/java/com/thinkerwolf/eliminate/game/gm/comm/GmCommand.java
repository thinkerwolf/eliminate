package com.thinkerwolf.eliminate.game.gm.comm;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GmCommand {
    /**
     * 获取所有GM列表
     */
    HELP("help", "获取GM列表"),
    REWARD("reward", "添加奖励"),
    CONSUME_REWARD("consumeReward", "消耗奖励"),
    STOP_BATTLE("stopBattle", "强制停止战斗"),
    REFRESH_SDATA("refreshSdata", "刷新静态数据"),
    ACTION("action", "强制推送玩家动作"),

    ;

    private final String cmd;
    private final String intro;

    GmCommand(String cmd, String intro) {
        this.cmd = cmd;
        this.intro = intro;
    }

    @JsonProperty(value = "cmd")
    public String getCmd() {
        return cmd;
    }

    @JsonProperty(value = "intro")
    public String getIntro() {
        return intro;
    }

    public static GmCommand cmdOf(String cmd) {
        for (GmCommand c : GmCommand.values()) {
            if (c.cmd.equals(cmd)) {
                return c;
            }
        }
        return null;
    }

}
