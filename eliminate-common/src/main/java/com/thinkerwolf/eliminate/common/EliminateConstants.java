package com.thinkerwolf.eliminate.common;

import com.thinkerwolf.gamer.common.DefaultThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public final class EliminateConstants {
    /**
     * 项目名称
     */
    public static final String GAME_NAME = "eliminate";

    public static String GAME_ID;

    /**
     * 通用ScheduledThreadPoolExecutor
     */
    public static final ScheduledThreadPoolExecutor COMMON_SCHEDULED
            = new ScheduledThreadPoolExecutor(10, new DefaultThreadFactory("CommonScheduled"));

}
