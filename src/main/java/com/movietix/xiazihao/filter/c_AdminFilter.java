package com.movietix.xiazihao.filter;

import com.movietix.xiazihao.constants.ConstantsManager;
import com.movietix.xiazihao.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = {"/*"})
public class c_AdminFilter implements Filter {
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

        // 放行普通用户接口
        if (httpRequest.getRequestURI().contains("/works")) {
            chain.doFilter(request, response);
            return;
        }
        //获取请求头的AdminToken
        String adminToken = httpRequest.getHeader("adminToken");
        log.info("adminToken:{}", adminToken);
        //验证Token
        if (adminToken == null || adminToken.isEmpty()) {
            throw new ServletException("你没有管理员权限");
        }
        try {
            JwtUtils.parseAdminToken(adminToken);
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
