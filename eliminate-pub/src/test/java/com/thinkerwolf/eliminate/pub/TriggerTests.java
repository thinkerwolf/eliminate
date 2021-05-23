package com.thinkerwolf.eliminate.pub;

import com.thinkerwolf.eliminate.pub.trigger.TriggerEvent;
import com.thinkerwolf.eliminate.pub.trigger.TriggerManager;
import com.thinkerwolf.eliminate.pub.trigger.TriggerType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TriggerTests {

    static Random r = new Random();

    static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(500);

    @Test
    public void testBase() {
        int[] targets = new int[]{1, 3, 9, 10, 20, 40, 50, 100, 70};
        for (int i = 0; i < 10000; i++) {
            final int param = i;
            final int target = targets[r.nextInt(targets.length)];
            final TriggerType type = TriggerType.values()[r.nextInt(TriggerType.values().length)];
            executor.execute(() -> TriggerManager.addTrigger(target, type, event -> {
                String s = String.format("Listener %s %s %d", event.getSource().toString(), Arrays.toString(event.getParams()), param);
                System.out.println(s);
                return r.nextBoolean();
            }));
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        for (int i = 0; i < 10000; i++) {
            final int param = i;
            final int target = targets[r.nextInt(targets.length)];
            final TriggerType type = TriggerType.values()[r.nextInt(TriggerType.values().length)];
            executor.execute(() -> TriggerManager.notify(target, type, new TriggerEvent(target, param)));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
    }

}
