package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.pojo.Log;
import com.movietix.xiazihao.entity.result.Result;
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
    private final LogService logService = new LogServiceImpl();

    // 封装返回方法
    private void sendResponse(HttpServletResponse resp, Result result) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(JsonUtils.toJson(result));
        out.flush();
    }

    //1.返回日志列表
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Log> logList = logService.selectAllLogs();
            log.info("查询到的日志列表:{}",logList);
            // 3. 将请求体作为响应返回
            sendResponse(resp, Result.success(logList));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //2.批量删除日志
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String[] id_strs = req.getParameterValues("ids");
            log.info("接收到的日志ID:{}", (Object) id_strs);
            logService.deleteLogByIds(id_strs);
            sendResponse(resp, Result.success());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
