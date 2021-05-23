package com.thinkerwolf.eliminate.gateway.net;

import com.thinkerwolf.eliminate.common.util.RequestUtils;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.remoting.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网关Session管理器
 *
 * @author wukai
 */
public class GatewaySessions {

    private static final GatewaySessions INSTANCE = new GatewaySessions();

    private GatewaySessions() {
    }

    public static GatewaySessions getInstance() {
        return INSTANCE;
    }

    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public void putSession(Session session) {
        sessionMap.put(session.getId(), session);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public Session getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public Session requestSession(Request request) {
        String sessionId = (String) request.getAttribute(RequestUtils.KEY_SESSION_ID);
        Channel channel = (Channel) request.getChannel();
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = (String) channel.getAttr(Session.JSESSION);
            if (sessionId == null) {
                return null;
            }
        } else {
            channel.setAttr(Session.JSESSION, sessionId);
        }
        Session session = getSession(sessionId);
        if (session != null && session.getPush() == null) {
            session.setPush(request.newPush());
        }
        return session;
    }

    public void push(String sessionId, int opcode, String command, byte[] obj) {
        Session session = sessionMap.get(sessionId);
        if (session != null) {
            session.push(opcode, command, obj);
        }
    }

}
