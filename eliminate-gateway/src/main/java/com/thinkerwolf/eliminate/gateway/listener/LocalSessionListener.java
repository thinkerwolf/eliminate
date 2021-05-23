package com.thinkerwolf.eliminate.gateway.listener;

import com.thinkerwolf.eliminate.gateway.net.GatewaySessions;
import com.thinkerwolf.gamer.core.servlet.SessionEvent;
import com.thinkerwolf.gamer.core.servlet.SessionListener;

public class LocalSessionListener implements SessionListener {
    @Override
    public void sessionCreated(SessionEvent se) {

    }

    @Override
    public void sessionExpired(SessionEvent se) {

    }

    @Override
    public void sessionDestroyed(SessionEvent se) {
        GatewaySessions.getInstance().removeSession(se.getSource().getId());
    }
}
