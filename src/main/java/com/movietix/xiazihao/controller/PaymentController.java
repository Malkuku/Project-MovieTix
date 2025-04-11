package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.PaymentService;
import com.movietix.xiazihao.service.impl.PaymentServiceImpl;
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

@Slf4j
@WebServlet("/payments/*")
public class PaymentController extends HttpServlet {
    private final PaymentService paymentService = new PaymentServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {

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
    }

    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                    req.getParameter("minAmount") != null ? new BigDecimal(req.getParameter("minAmount")) : null
            );
            param.setMaxAmount(
                    req.getParameter("maxAmount") != null ? new BigDecimal(req.getParameter("maxAmount")) : null
            );
            param.setPage(
                    req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1
            );
            param.setPageSize(
                    req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : 10
            );
        }
        log.info("接收到的查询参数:{}", param);
        PageResult<Payment> pageResult = paymentService.selectPaymentsByPage(param);
        log.info("查询到的支付记录信息:{}", pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }
}
