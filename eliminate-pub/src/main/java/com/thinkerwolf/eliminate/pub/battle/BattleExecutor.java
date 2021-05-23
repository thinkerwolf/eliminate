package com.thinkerwolf.eliminate.pub.battle;

import com.thinkerwolf.gamer.common.DefaultThreadFactory;
import org.apache.commons.lang.math.NumberUtils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 战斗执行器
 *
 * @author wukai
 */
public class BattleExecutor {

    private static ScheduledThreadPoolExecutor[] executors;

    static {
        int num = NumberUtils.toInt(System.getenv("battle.executor.num"), 10);
        executors = new ScheduledThreadPoolExecutor[num];
        ThreadFactory threadFactory = new DefaultThreadFactory("BattleExec", true);
        for (int i = 0; i < num; i++) {
            executors[i] = new ScheduledThreadPoolExecutor(1, threadFactory);
        }
    }

    private final ScheduledThreadPoolExecutor executor;

    public BattleExecutor() {
        this.executor = executors[Math.abs(hashCode()) % executors.length];
    }

    public void addEvent(BattleEvent event) {
        long delay = event.executeTime - event.getUnit().convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        executor.schedule(event, Math.max(delay, 0), event.getUnit());
    }

    public void cancelEvent(BattleEvent event) {
        event.cancel();
    }

}
