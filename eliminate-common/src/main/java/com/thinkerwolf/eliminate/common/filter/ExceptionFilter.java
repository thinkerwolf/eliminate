package com.thinkerwolf.eliminate.common.filter;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.Invocation;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.*;
import com.thinkerwolf.gamer.core.util.ResponseUtil;

import java.util.Map;

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
            writeRequestLog(request);
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

    private void writeRequestLog(Request request) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Command [").append(request.getCommand())
                    .append("] Params[");
            for (Map.Entry<String, Object> en : request.getAttributes().entrySet()) {
                sb.append(en.getKey()).append("=").append(en.getValue()).append(",");
            }
            sb.append("]");
            LOG.info("Invoke {}", sb.toString());
        } catch (Exception ignored) {

        }
    }

    @Override
    public void destroy() throws Exception {

    }
}
