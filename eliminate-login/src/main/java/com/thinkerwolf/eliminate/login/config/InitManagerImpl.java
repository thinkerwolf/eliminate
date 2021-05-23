package com.thinkerwolf.eliminate.login.config;

import com.thinkerwolf.eliminate.common.config.AbstractInitManager;
import com.thinkerwolf.eliminate.login.data.DataBus;
import com.thinkerwolf.eliminate.login.data.DataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.SDataBus;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitManagerImpl extends AbstractInitManager {

    private static final Logger LOG = InternalLoggerFactory.getLogger(InitManagerImpl.class);

    @Autowired
    DataBus dataBus;
    @Autowired
    SDataBus sDataBus;

    @Override
    protected void doInit() throws Exception {
        try {
            DataBusManager.init(dataBus);
            SDataBusManager.init(sDataBus);
        } catch (Exception e) {
            LOG.error("Project init", e);
        }
    }

}
