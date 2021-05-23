package com.thinkerwolf.eliminate.game.filter;

import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.OpState;
import com.thinkerwolf.eliminate.common.util.RequestUtils;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.user.UserDto;
import com.thinkerwolf.eliminate.pub.user.Users;
import com.thinkerwolf.gamer.core.mvc.Invocation;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.*;
import com.thinkerwolf.gamer.core.util.ResponseUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏服Session过滤器
 *
 * @author wukai
 */
public class GameSessionFilter implements Filter {

    enum ActionType {
        /**
         * 验证用户是否登录
         */
        USER,
        /**
         * 验证用户是否登录
         */
        PLAYER,
        /**
         * 不进行任何限制
         */
        IGNORE,
    }

    private static final Map<String, ActionType> command2Types = new HashMap<>();

    static {
        command2Types.put("user@login", ActionType.IGNORE);
        command2Types.put("player@login", ActionType.USER);
        command2Types.put("player@gwLogin", ActionType.PLAYER);
        command2Types.put("saveScript", ActionType.IGNORE);
        command2Types.put("getScript", ActionType.IGNORE);
    }

    @Override
    public void init(ServletConfig servletConfig) throws Exception {
    }

    @Override
    public void doFilter(Invocation invocation, Request request, Response response, FilterChain filterChain) throws Exception {
        ActionType type = getType(invocation, request, response);
        switch (type) {
            case USER:
                Session session = getSession(request);
                UserDto userDto = Users.getDto(session);
                if (userDto == null) {
                    OpResult op = OpResult.build(OpState.NO_LOGIN, null, LocalMessages.T_SESSION_1);
                    op.setRequestId(request.getRequestId());
                    ResponseUtil.render(ResponseUtil.JSON_VIEW, new JsonModel(op), request, response);
                    return;
                }
                break;
            case PLAYER:
                session = getSession(request);
                PlayerDto playerDto = Players.getDto(session);
                if (playerDto == null) {
                    OpResult op = OpResult.build(OpState.NO_LOGIN, null, LocalMessages.T_SESSION_1);
                    op.setRequestId(request.getRequestId());
                    ResponseUtil.render(ResponseUtil.JSON_VIEW, new JsonModel(op), request, response);
                    return;
                }
                break;
            default:
                break;
        }
        filterChain.doFilter(invocation, request, response);
    }

    private Session getSession(Request request) {
        Session session = RequestUtils.getSession(request);
        if (session != null) {
            if (session.getPush() == null) {
                session.setPush(request.newPush());
            }
        }
        return session;
    }

    private ActionType getType(Invocation invocation, Request request, Response response) {
        if (RequestUtils.isGatewayRequest(request)) {
            return ActionType.PLAYER;
        }
        if (RequestUtils.isRpcRequest(request)
                || RequestUtils.isSwaggerRequest(request)
                || RequestUtils.isResourceRequest(invocation)) {
            return ActionType.IGNORE;
        }
        String command = request.getCommand();
        ActionType type = command2Types.get(command);
        if (type == null) {
            return ActionType.PLAYER;
        }
        return type;
    }

    @Override
    public void destroy() throws Exception {

    }
}
