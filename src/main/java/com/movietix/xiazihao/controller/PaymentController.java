package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.PaymentService;
import com.movietix.xiazihao.service.impl.PaymentServiceImpl;
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

@Slf4j
@WebServlet("/payments/*")
public class PaymentController extends HttpServlet {
    private final PaymentService paymentService = new PaymentServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            addPayment(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            try {
                selectPaymentById(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                selectPaymentsByPage(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.matches("/\\d+/notify")){
            try {
                updatePayment(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    //更新支付记录
    private void updatePayment(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String pathInfo = req.getPathInfo();
        Integer id = Integer.parseInt(pathInfo.substring(1,pathInfo.indexOf("/notify")));
        String json = ServletUtils.getRequestBody(req);
        Payment payment = JsonUtils.parseJson(json, Payment.class);
        payment.setId(id);
        log.info("接收到的支付记录信息:{}", payment);
        paymentService.updatePayment(payment);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //创建支付记录
    private void addPayment(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String json = ServletUtils.getRequestBody(req);
        Payment payment = JsonUtils.parseJson(json, Payment.class);
        log.info("接收到的支付记录信息:{}", payment);
        paymentService.addPayment(payment);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //条件分页查询支付记录
    private void selectPaymentsByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        PaymentQueryParam param = new PaymentQueryParam();
        {
            param.setOrderId(
                    req.getParameter("orderId") != null ? Integer.parseInt(req.getParameter("orderId")) : null
            );
            param.setTransactionId(req.getParameter("transactionId"));
            param.setPaymentMethod(req.getParameter("paymentMethod"));
            param.setStatus(
                    req.getParameter("status") != null ? Integer.parseInt(req.getParameter("status")) : null
            );
            param.setStartDate(req.getParameter("startDate"));
            param.setEndDate(req.getParameter("endDate"));
            param.setMinAmount(
                    req.getParameter("minAmount") != null ? Double.valueOf(req.getParameter("minAmount")) : null
            );
            param.setMaxAmount(
                    req.getParameter("maxAmount") != null ? Double.valueOf(req.getParameter("maxAmount")) : null
            );
            param.setPage(
                    req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : param.getPage()
            );
            param.setPageSize(
                    req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : param.getPageSize()
            );
        }
        log.info("接收到的查询参数:{}", param);
        PageResult<Payment> pageResult = paymentService.selectPaymentsByPage(param);
        log.info("查询到的支付记录信息:{}", pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }

    //获取支付记录详情
    private void selectPaymentById(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String pathInfo = req.getPathInfo();
        Integer id = Integer.parseInt(pathInfo.substring(1));
        log.info("接收到的支付记录ID:{}", id);
        Payment payment = paymentService.selectPaymentById(id);
        log.info("查询到的支付记录信息:{}", payment);
        ServletUtils.sendResponse(resp, Result.success(payment));
    }
}
