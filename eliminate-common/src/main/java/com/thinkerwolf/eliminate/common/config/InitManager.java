package com.thinkerwolf.eliminate.common.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

public interface InitManager extends InitializingBean, ApplicationContextAware {

    void init() throws Exception;

    String getGamerMyId();
}
