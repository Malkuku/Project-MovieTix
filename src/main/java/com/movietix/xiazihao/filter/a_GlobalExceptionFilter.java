package com.movietix.xiazihao.filter;

import com.movietix.xiazihao.constants.ConstantsManager;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class a_GlobalExceptionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(ConstantsManager.getInstance().getFilterSwitch()){
            chain.doFilter(request, response);
            return;
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("全局异常捕获: {}",e.getMessage());
            ServletUtils.sendResponse((HttpServletResponse) response, Result.error(e.getMessage()));
        }
    }
}
