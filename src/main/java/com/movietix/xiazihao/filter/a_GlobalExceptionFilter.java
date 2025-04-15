package com.movietix.xiazihao.filter;

import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.utils.ServletUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter("/*")
public class a_GlobalExceptionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            ServletUtils.sendResponse((HttpServletResponse) response, Result.error("操作失败，请联系管理员"));
        }
    }
}
