package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.OrderService;
import com.movietix.xiazihao.service.impl.OrderServiceImpl;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
@WebServlet("/orders/*")
public class OrderController extends HttpServlet {
    private final static OrderService orderService = new OrderServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            selectOrderById(req, resp);
        }else{
            try {
                selectOrdersByPage(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.matches("")){

        }else{

        }
    }

    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    //分页查询订单数据
    private void selectOrdersByPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        OrderQueryParam param = new OrderQueryParam();
        {
            param.setOrderNo(req.getParameter("orderNo"));
            param.setUserId(req.getParameter("userId") != null ? Integer.parseInt(req.getParameter("userId")) : null);
            param.setScreeningId(req.getParameter("screeningId") != null ? Integer.parseInt(req.getParameter("screeningId")) : null);
            param.setStatus(req.getParameter("status") != null ? Integer.parseInt(req.getParameter("status")) : null);
            param.setMinAmount(req.getParameter("minAmount") != null ? new BigDecimal(req.getParameter("minAmount")) : null);
            param.setMaxAmount(req.getParameter("maxAmount") != null ? new BigDecimal(req.getParameter("maxAmount")) : null);
            param.setStartDate(req.getParameter("startDate") != null ? LocalDateTime.parse(req.getParameter("startDate")) : null);
            param.setEndDate(req.getParameter("endDate") != null ? LocalDateTime.parse(req.getParameter("endDate")) : null);
            param.setPage(req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1);
            param.setPageSize(req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : 10);
        }
        log.info("接收到的订单查询参数:{}", param);
        PageResult<Order> pageResult = orderService.selectOrdersByPage(param);
        log.info("查询到的订单信息:{}", pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }

    //TODO 创建订单

    //根据id查询订单
    private void selectOrderById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Integer id = Integer.parseInt(pathInfo.substring(1));
        try {
            Order order = orderService.selectOrderById(id);
            if (order != null) {
                ServletUtils.sendResponse(resp, Result.success(order));
            } else {
                ServletUtils.sendResponse(resp, Result.error("订单不存在"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
