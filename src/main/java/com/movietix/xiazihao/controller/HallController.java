package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.HallService;
import com.movietix.xiazihao.service.impl.HallServiceImpl;
import com.movietix.xiazihao.utils.JsonUtils;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@WebServlet("/halls/*")
public class HallController extends HttpServlet {
    private final HallService hallService = new HallServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addHall(req, resp);
    }
    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            selectHallById(req, resp);
        }else{
            selectHallsByPage(req, resp);
        }
    }
    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updateHall(req, resp);
    }
    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        deleteHallsByIds(req, resp);
    }

    //TODO 批量更新放映厅状态

    //根据ID查询放映厅
    private void selectHallById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String idStr = pathInfo.substring(1);
        Integer id = Integer.valueOf(idStr);
        Hall hall = hallService.selectHallById(id);
        log.info("查询影厅成功, 影厅id: {}, 查询结果: {}", id, hall);
        ServletUtils.sendResponse(resp, Result.success(hall));
    }

    //修改放映厅信息
    private void updateHall(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = ServletUtils.getRequestBody(req);
        Hall hall = JsonUtils.parseJson(json, Hall.class);
        hallService.updateHall(hall);
        log.info("修改影厅成功, 影厅信息: {}", hall);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //通过id批量删除放映厅
    private void deleteHallsByIds(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] values = req.getParameterValues("ids");
        List<Integer> ids = new ArrayList<>();
        {
            if(values != null && values.length > 0) {
                String[] id_strs = values[0].split(",");
                for (String id_str : id_strs) {
                    ids.add(Integer.valueOf(id_str));
                }
            }
        }
        hallService.deleteHallsByIds(ids);
        log.info("批量删除影厅成功, 影厅id: {}", ids);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //添加放映厅
    private void addHall(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = ServletUtils.getRequestBody(req);
        Hall hall = JsonUtils.parseJson(json, Hall.class);
        hallService.addHall(hall);
        log.info("添加影厅成功, 影厅信息: {}", hall);
        ServletUtils.sendResponse(resp, Result.success());
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