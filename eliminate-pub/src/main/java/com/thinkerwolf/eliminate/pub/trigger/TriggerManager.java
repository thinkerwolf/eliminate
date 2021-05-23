package com.thinkerwolf.eliminate.pub.trigger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 触发器管理器
 *
 * @author wukai
 */
public class TriggerManager {

    private static Map<Object, TriggerCollection> targetTriggerMap = new ConcurrentHashMap<>();

    public static void addTrigger(Object target, final TriggerType triggerType, final TriggerListener listener) {
        TriggerCollection collection = targetTriggerMap.computeIfAbsent(target, (k) -> new TriggerCollection(target));
        collection.addTrigger(triggerType, listener);
    }


    public static void removeTrigger(Object target, final TriggerType type, TriggerListener listener) {
        TriggerCollection collection = targetTriggerMap.get(target);
        if (collection != null) {
            collection.removeTrigger(type, listener);
        }
    }

    public static void removeAllTrigger(Object target, final TriggerType type) {
        TriggerCollection collection = targetTriggerMap.get(target);
        if (collection != null) {
            collection.removeAllTrigger(type);
        }
    }

    public static void notify(Object target, TriggerType triggerType, TriggerEvent event) {
        TriggerCollection collection = targetTriggerMap.get(target);
        if (collection != null) {
            collection.notify(triggerType, event);
        }
    }

}
