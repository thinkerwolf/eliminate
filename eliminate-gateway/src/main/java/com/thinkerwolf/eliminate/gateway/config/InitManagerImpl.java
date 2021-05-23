package com.thinkerwolf.eliminate.gateway.config;

import com.thinkerwolf.eliminate.common.config.AbstractInitManager;
import com.thinkerwolf.eliminate.gateway.databus.DataBus;
import com.thinkerwolf.eliminate.gateway.databus.DataBusManager;
import com.thinkerwolf.gamer.core.servlet.ServletBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitManagerImpl extends AbstractInitManager {

    @Autowired
    private DataBus dataBus;

    @Override
    protected void doInit() throws Exception {
        DataBusManager.init(dataBus);
    }


}
