package com.thinkerwolf.eliminate.pub.trigger;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class TriggerCollection {

    private final Object target;

    private final Map<TriggerType, List<TriggerListener>> typeListeners = new ConcurrentHashMap<>();

    public TriggerCollection(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public void addTrigger(TriggerType type, TriggerListener listener) {
        List<TriggerListener> events = typeListeners.computeIfAbsent(type, (k) -> new Vector<>());
        events.add(listener);
    }

    public void removeTrigger(TriggerType type, TriggerListener listener) {
        List<TriggerListener> events = typeListeners.get(type);
        if (events != null) {
            events.remove(listener);
        }
    }

    public void removeAllTrigger(TriggerType type) {
        List<TriggerListener> events = typeListeners.get(type);
        if (events != null) {
            events.clear();
        }
    }

    public void notify(TriggerType type, final TriggerEvent event) {
        List<TriggerListener> listeners = typeListeners.get(type);
        if (listeners != null) {
            List<TriggerListener> copyListeners = Lists.newArrayList(listeners);
            List<TriggerListener> removeListeners = Lists.newLinkedList();
            for (TriggerListener listener : copyListeners) {
                if (listener.handleEvent(event)) {
                    removeListeners.add(listener);
                }
            }
            listeners.removeAll(removeListeners);
        }
    }

}
