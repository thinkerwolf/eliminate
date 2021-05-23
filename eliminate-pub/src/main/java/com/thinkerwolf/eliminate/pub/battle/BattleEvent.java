package com.thinkerwolf.eliminate.pub.battle;

import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 战斗事件
 *
 * @author wukai
 */
public abstract class BattleEvent implements Runnable {

    private static final Logger logger = InternalLoggerFactory.getLogger(BattleEvent.class);

    long executeTime;

    private AtomicBoolean stop;

    private TimeUnit unit;

    public BattleEvent(long delay, TimeUnit unit) {
        this.executeTime = unit.convert(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(delay, unit), TimeUnit.MILLISECONDS);
        this.unit = unit;
        this.stop = new AtomicBoolean(false);
    }

    public BattleEvent(long delay) {
        this(delay, TimeUnit.MILLISECONDS);
    }

    public BattleEvent() {
        this(0, TimeUnit.MILLISECONDS);
    }

    public TimeUnit getUnit() {
        return unit;
    }

    @Override
    public void run() {
        if (stop.get()) {
            return;
        }
        try {
            doRun();
        } catch (Exception e) {
            logger.error("Catch exception", e);
        }
    }

    protected abstract void doRun();

    /**
     * 取消事件
     */
    public void cancel() {
        stop.compareAndSet(false, true);
    }

}
