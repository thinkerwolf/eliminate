package com.thinkerwolf.eliminate.filter;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.Invocation;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.*;
import com.thinkerwolf.gamer.core.util.ResponseUtil;

/**
 * 异常捕获过滤器
 *
 * @author wukai
 */
public class ExceptionFilter implements Filter {
    private static final Logger LOG = InternalLoggerFactory.getLogger(ExceptionFilter.class);

    @Override
    public void init(ServletConfig servletConfig) throws Exception {

    }

    @Override
    public void doFilter(Invocation invocation, Request request, Response response, FilterChain filterChain) {
        try {
            LOG.info("Invoke command [" + invocation.getCommand() + "]");
            filterChain.doFilter(invocation, request, response);
        } catch (Exception e) {
            LOG.error("Exception caught", e);
            OpResult op = OpResult.fail("E1101");
            op.setRequestId(request.getRequestId());
            try {
                ResponseUtil.render(ResponseUtil.JSON_VIEW, new JsonModel(op), request, response);
            } catch (Exception t) {
                LOG.error("", t);
            }
        }
    }

    @Override
    public void destroy() throws Exception {

    }
}
