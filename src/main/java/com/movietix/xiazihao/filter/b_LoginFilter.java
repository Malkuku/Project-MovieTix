package com.movietix.xiazihao.filter;

import com.movietix.xiazihao.constants.ConstantsManager;
import com.movietix.xiazihao.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = {"/*"}) //TODO是否有更高效的过滤器机制？
public class b_LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if(ConstantsManager.getInstance().getFilterSwitch()){
            chain.doFilter(request, response);
            return;
        }

        // 放行登录接口 和 注册接口
        if (httpRequest.getRequestURI().contains("/login")
                || httpRequest.getRequestURI().contains("/register")) {
            chain.doFilter(request, response);
            return;
        }
        // 放行游客 GET 请求
        if (httpRequest.getRequestURI().contains("/works/") && "GET".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        //获取请求头的Token
        String token = httpRequest.getHeader("token");
        log.info("Token:{}", token);
        //验证Token
        if (token == null || token.isEmpty()) {
            throw new ServletException("Token不能为空");
        }
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            throw new ServletException("Token错误");
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