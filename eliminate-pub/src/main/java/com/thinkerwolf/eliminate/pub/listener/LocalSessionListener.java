package com.thinkerwolf.eliminate.pub.listener;

import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.user.Users;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.core.servlet.SessionEvent;
import com.thinkerwolf.gamer.core.servlet.SessionListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Session监听器
 *
 * @author wukai
 */
public class LocalSessionListener implements SessionListener {

    private static final Logger LOG = InternalLoggerFactory.getLogger(LocalSessionListener.class);

    private Set<Session> sessions = new LinkedHashSet<>();
    private final Object lock = new Object();

    @Override
    public void sessionCreated(SessionEvent se) {
        synchronized (lock) {
            sessions.add(se.getSource());
        }
    }

    @Override
    public void sessionExpired(SessionEvent se) {

    }

    @Override
    public void sessionDestroyed(SessionEvent se) {
        LOG.debug("Session destory : {}", se.getSource());
        synchronized (lock) {
            sessions.remove(se.getSource());
        }
        Players.removeDto(se.getSource());
        Users.removeDto(se.getSource());
    }

}
