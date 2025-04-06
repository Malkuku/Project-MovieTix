package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.Log;
import com.movietix.xiazihao.service.LogService;
import com.movietix.xiazihao.service.impl.LogServiceImpl;
import com.movietix.xiazihao.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.io.PrintWriter;

@Slf4j
@WebServlet("/logs")
public class LogController extends HttpServlet {
    private LogService logService = new LogServiceImpl();

    //1.返回日志列表
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Log> logList = logService.selectAllLogs();
            log.info("查询到的日志列表:{}",logList);
            // 1. 设置响应类型（如 JSON/Text）
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            // 3. 将请求体作为响应返回
            String json = JsonUtils.toJson(logList);
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
