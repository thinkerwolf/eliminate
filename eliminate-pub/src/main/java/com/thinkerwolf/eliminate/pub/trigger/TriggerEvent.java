package com.thinkerwolf.eliminate.pub.trigger;

import java.util.EventObject;

/**
 * 触发器事件
 *
 * @author wukai
 */
public class TriggerEvent extends EventObject {

    private Object[] params;

    public TriggerEvent(Object source, Object... params) {
        super(source);
        this.params = params;
    }

    public Object[] getParams() {
        return params;
    }
}
