package com.thinkerwolf.eliminate.login.filter;

import com.thinkerwolf.gamer.core.mvc.Invocation;
import com.thinkerwolf.gamer.core.servlet.*;

public class LoginSessionFilter implements Filter {
    @Override
    public void init(ServletConfig servletConfig) throws Exception {

    }

    @Override
    public void doFilter(Invocation invocation, Request request, Response response, FilterChain filterChain) throws Exception {
        filterChain.doFilter(invocation, request, response);
    }

    @Override
    public void destroy() throws Exception {

    }
}
