package com.thinkerwolf.eliminate.pub.trigger;

import java.util.EventListener;

/**
 * @athor wukai
 * @since 2020-05-25
 */
public interface TriggerListener extends EventListener {

    /**
     * 处理事件
     *
     * @param event
     * @return 是否删除
     */
    boolean handleEvent(TriggerEvent event);

}
