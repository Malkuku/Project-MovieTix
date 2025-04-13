package com.movietix.xiazihao.filter;

import com.movietix.xiazihao.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = {"/*"})
public class a_LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 放行登录接口
        if (httpRequest.getRequestURI().contains("/users/login")) {
            chain.doFilter(request, response);
            return;
        }
        //获取请求头的Token
        String token = httpRequest.getHeader("token");
        //验证Token
        if (token == null || token.isEmpty()) {
            log.info("Token为空");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token不能为空");
            return;
        }
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            log.info("Token错误");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token错误");
        }

        //放行请求
        log.debug("Token验证通过");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.debug("过滤器销毁");
    }
}