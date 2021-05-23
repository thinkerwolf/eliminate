package com.thinkerwolf.eliminate.common.config;

import com.thinkerwolf.eliminate.common.EliminateConstants;
import com.thinkerwolf.gamer.common.Constants;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.core.servlet.ServletBootstrap;
import com.thinkerwolf.gamer.core.servlet.SessionManager;
import com.thinkerwolf.gamer.registry.Registry;
import com.thinkerwolf.gamer.registry.RegistryState;
import com.thinkerwolf.gamer.registry.StateListenerAdapter;
import com.thinkerwolf.gamer.remoting.Protocol;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

public abstract class AbstractInitManager implements InitManager {
    private ApplicationContext applicationContext;

    private SessionManager sessionManager;
    private String gamerMyId;

    @Value("${eliminate.id:}")
    private String springGamerId;

    @Value("${eliminate.type:unknown}")
    private String serverType;

    @Autowired(required = false)
    private Registry registry;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public String getServerType() {
        return serverType;
    }

    @Override
    public String getGamerMyId() {
        return gamerMyId;
    }

    public Registry getRegistry() {
        return registry;
    }

    @Override
    public void init() throws Exception {
        processGamerId();
        registryServer();
        initSessionManager();
        doInit();
    }

    /** 处理应用服务器ID，用于服务注册 */
    protected void processGamerId() {
        gamerMyId = System.getenv(Constants.ENV_GAMER_MY_ID);
        if (StringUtils.isNotBlank(gamerMyId)) {
            return;
        }
        gamerMyId = System.getProperty(Constants.JVM_GAMER_MY_ID);
        if (StringUtils.isNotBlank(gamerMyId)) {
            return;
        }
        gamerMyId = springGamerId;
        if (StringUtils.isNotBlank(gamerMyId)) {
            return;
        }
        gamerMyId = RandomStringUtils.randomAlphanumeric(8);
    }

    protected void initSessionManager() {
        ServletBootstrap bootstrap = getApplicationContext().getBean(ServletBootstrap.class);
        this.sessionManager = bootstrap.getServletConfig().getServletContext().getSessionManager();
        SessionManagerHolder.set(sessionManager);
    }

    /** 将本应用服务器地址注册到注册中心 */
    protected void registryServer() {
        if (registry != null) {
            ServletBootstrap bootstrap = applicationContext.getBean(ServletBootstrap.class);
            registerSelf(bootstrap, registry);
            registry.subscribeState(
                    new StateListenerAdapter() {
                        @Override
                        public void notifyStateChange(RegistryState state) {
                            System.err.println("State change " + state);
                            if (RegistryState.CONNECTED.equals(state)) {
                                registerSelf(bootstrap, registry);
                            }
                        }
                    });
        }
    }

    protected abstract void doInit() throws Exception;

    protected void registerSelf(ServletBootstrap bootstrap, Registry registry) {
        EliminateConstants.COMMON_SCHEDULED.schedule(
                () -> {
                    for (URL url : bootstrap.getUrls()) {
                        Protocol protocol = Protocol.parseOf(url.getProtocol());
                        url.setPath(
                                registry.url().getPath()
                                        + "/"
                                        + serverType
                                        + "/"
                                        + protocol.getName());
                        url.getParameters().put(URL.NODE_EPHEMERAL, true);
                        url.getParameters()
                                .put(
                                        URL.NODE_NAME,
                                        protocol.getName() + ":" + serverType + "_" + gamerMyId);
                        registry.register(url);
                    }
                },
                500,
                TimeUnit.MILLISECONDS);
    }
}
