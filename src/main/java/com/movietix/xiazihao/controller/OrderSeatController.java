package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.pojo.OrderSeat;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.OrderSeatService;
import com.movietix.xiazihao.service.impl.OrderSeatServiceImpl;
import com.movietix.xiazihao.utils.JsonUtils;
import com.movietix.xiazihao.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@WebServlet("/order_seats/*")
public class OrderSeatController extends HttpServlet {
    private final OrderSeatService orderSeatService = new OrderSeatServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addOrderSeats(req, resp);
    }

    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            selectOrderSeatById(req, resp);
        }else if(pathInfo != null && pathInfo.matches("/by_order/\\d+")){
            selectOrderSeatsByOrderId(req, resp);
        }else {
            try {
                selectOrderSeatsByCondition(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updateOrderSeats(req, resp);
    }

    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    //根据订单批量查询
    private void selectOrderSeatsByOrderId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Integer orderId = Integer.parseInt(pathInfo.substring(pathInfo.indexOf("/by_order/")+"/by_order/".length()));
        try {
            List<OrderSeat>  orderSeatList = orderSeatService.selectOrderSeatsByOrderId(orderId);
            log.info("orderSeatList: {}", orderSeatList);
            ServletUtils.sendResponse(resp, Result.success(orderSeatList));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //根据复合条件查询座位
    private void selectOrderSeatsByCondition(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String orderId = req.getParameter("orderId");
        String seatRow = req.getParameter("seatRow");
        String seatCol = req.getParameter("seatCol");
        OrderSeat orderSeat = orderSeatService.selectOrderSeatsByCondition(orderId, seatRow, seatCol);
        log.info("orderSeat: {}", orderSeat);
        ServletUtils.sendResponse(resp, Result.success(orderSeat));
    }

    //根据id查询座位
    private void selectOrderSeatById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        int id = Integer.parseInt(pathInfo.substring(1));
        OrderSeat orderSeat = null;
        try {
            orderSeat = orderSeatService.selectOrderSeatById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ServletUtils.sendResponse(resp, Result.success(orderSeat));
    }

    //批量添加座位
    private void addOrderSeats(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = ServletUtils.getRequestBody(req);
        List<OrderSeat> orderSeatList = JsonUtils.parseJsonToList(json, OrderSeat.class);
        log.info("orderSeatList: {}", orderSeatList);
        try {
            orderSeatService.addOrderSeats(orderSeatList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ServletUtils.sendResponse(resp, Result.success());
    }

    //更新座位信息
    private void updateOrderSeats(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = ServletUtils.getRequestBody(req);
        OrderSeat orderSeat = JsonUtils.parseJson(json, OrderSeat.class);
        log.info("orderSeat: {}", orderSeat);
        try {
            orderSeatService.updateOrderSeats(orderSeat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ServletUtils.sendResponse(resp, Result.success());
    }
}
