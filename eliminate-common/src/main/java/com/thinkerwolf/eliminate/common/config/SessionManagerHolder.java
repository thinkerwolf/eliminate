package com.thinkerwolf.eliminate.common.config;

import com.thinkerwolf.gamer.core.servlet.SessionManager;

public class SessionManagerHolder {

    private static SessionManager sessionManager;

    public static SessionManager get() {
        return sessionManager;
    }

    public static void set(SessionManager sessionManager) {
        SessionManagerHolder.sessionManager = sessionManager;
    }
}
