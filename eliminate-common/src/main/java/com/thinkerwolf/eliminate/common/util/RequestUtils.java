package com.thinkerwolf.eliminate.common.util;

import com.thinkerwolf.eliminate.common.config.SessionManagerHolder;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.Invocation;
import com.thinkerwolf.gamer.core.mvc.ResourceInvocation;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Session;

public final class RequestUtils {

    public static final String KEY_SESSION_ID = "sessionId";

    public static boolean isGatewayRequest(Request request) {
        return request.getAttribute("gateway") != null;
    }

    public static boolean isRpcRequest(Request request) {
        return request.getCommand().startsWith("rpc@");
    }

    public static boolean isSwaggerRequest(Request request) {
        String command = request.getCommand();
        return command.contains("swagger-resources") || command.contains("api-docs");
    }

    public static boolean isResourceRequest(Invocation invocation) {
        return invocation instanceof ResourceInvocation;
    }

    public static Session getSession(Request request) {
        if (isGatewayRequest(request)) {
            String sessionId = (String) request.getAttribute(KEY_SESSION_ID);
            return SessionManagerHolder.get().getSession(sessionId, false);
        }
        return request.getSession(false);
    }

    public static Session newSession(Request request) {
        if (isGatewayRequest(request)) {
            String sessionId = (String) request.getAttribute(KEY_SESSION_ID);
            return SessionManagerHolder.get().getSession(sessionId, true);
        }
        return request.getSession(true);
    }

}
