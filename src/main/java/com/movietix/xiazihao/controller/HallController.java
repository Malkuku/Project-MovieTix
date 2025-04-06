package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.HallService;
import com.movietix.xiazihao.service.impl.HallServiceImpl;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet("/halls/*")
public class HallController extends HttpServlet {
    private final HallService hallService = new HallServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        selectHallsByPage(req, resp);
    }
    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    //分页查询
    private void selectHallsByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HallQueryParam param = new HallQueryParam();
        {
            param.setName(req.getParameter("name"));
            param.setMinCapacity(
                    req.getParameter("minCapacity") != null ? Integer.valueOf(req.getParameter("minCapacity")) : null
            );param.setMaxCapacity(
                    req.getParameter("maxCapacity") != null ? Integer.valueOf(req.getParameter("maxCapacity")) : null
            );param.setMinRows(
                    req.getParameter("minRows") != null ? Integer.valueOf(req.getParameter("minRows")) : null
            );param.setMinCols(
                    req.getParameter("minCols") != null ? Integer.valueOf(req.getParameter("minCols")) : null
            );param.setStatus(
                    req.getParameter("status") != null ? Integer.valueOf(req.getParameter("status")) : null
            );param.setPage(
                    req.getParameter("page") != null ? Integer.valueOf(req.getParameter("page")) : null
            );param.setPageSize(
                    req.getParameter("pageSize") != null ? Integer.valueOf(req.getParameter("pageSize")) : null
            );
       }
        PageResult<Hall> pageResult = hallService.selectHallsByPage(param);
        log.info("查询影厅成功, 查询条件: {}, 查询结果: {}", param, pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }
}