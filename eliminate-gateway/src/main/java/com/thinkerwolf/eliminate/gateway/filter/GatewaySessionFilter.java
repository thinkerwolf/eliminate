package com.thinkerwolf.eliminate.gateway.filter;

import com.thinkerwolf.eliminate.common.LocalMessages;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.OpState;
import com.thinkerwolf.eliminate.common.util.RequestUtils;
import com.thinkerwolf.eliminate.gateway.net.GatewaySessions;
import com.thinkerwolf.gamer.core.mvc.Invocation;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.*;
import com.thinkerwolf.gamer.core.util.ResponseUtil;


/**
 * 网关Session过滤
 *
 * @author wukai
 */
public class GatewaySessionFilter implements Filter {

    @Override
    public void init(ServletConfig servletConfig) throws Exception {
    }

    @Override
    public void doFilter(Invocation invocation, Request request, Response response, FilterChain filterChain) throws Exception {
        if (RequestUtils.isRpcRequest(request)) {
            filterChain.doFilter(invocation, request, response);
            return;
        }
        Session session = GatewaySessions.getInstance().requestSession(request);
        if (session == null) {
            OpResult op = OpResult.build(OpState.NO_LOGIN, "", LocalMessages.T_SESSION_1);
            op.setRequestId(request.getRequestId());
            ResponseUtil.render(ResponseUtil.JSON_VIEW, new JsonModel(op), request, response);
            return;
        }
        session.touch();
        filterChain.doFilter(invocation, request, response);
    }

    @Override
    public void destroy() throws Exception {
    }
}
