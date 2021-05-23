package com.thinkerwolf.eliminate.game.filter;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.core.mvc.Invocation;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.*;
import com.thinkerwolf.gamer.core.util.ResponseUtil;

/**
 * 文档拦截器，（文档开关）
 *
 * @author wukai
 */
public class DocsFilter implements Filter {

    private boolean docsEnable;

    @Override
    public void init(ServletConfig servletConfig) throws Exception {
        try {
            this.docsEnable = Boolean.parseBoolean(servletConfig.getInitParam("docsEnable"));
        } catch (Exception e) {
            this.docsEnable = false;
        }
    }

    @Override
    public void doFilter(Invocation invocation, Request request, Response response, FilterChain filterChain) throws Exception {
        boolean docsReq = request.getCommand().startsWith("docs");
        if (!docsReq) {
            filterChain.doFilter(invocation, request, response);
            return;
        }
        if (!docsEnable) {
            OpResult op = OpResult.fail("Doc system closed");
            op.setRequestId(request.getRequestId());
            ResponseUtil.render(ResponseUtil.JSON_VIEW, new JsonModel(op), request, response);
            return;
        }
        filterChain.doFilter(invocation, request, response);
    }

    @Override
    public void destroy() throws Exception {

    }
}
