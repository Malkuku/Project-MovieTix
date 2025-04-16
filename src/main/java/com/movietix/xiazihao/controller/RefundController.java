package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.param.RefundQueryParam;
import com.movietix.xiazihao.entity.pojo.Refund;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.RefundService;
import com.movietix.xiazihao.service.impl.RefundServiceImpl;
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
@WebServlet("/refunds/*")
public class RefundController extends HttpServlet {
    private static final RefundService refundService = new RefundServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createRefund(req, resp);
    }

    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.matches("/\\d+")){
            try {
                selectRefundsByUserId(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                selectRefundsByPage(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRefund(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    //条件分页查询退票申请
    private void selectRefundsByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        RefundQueryParam refundQueryParam = new RefundQueryParam();
        {
            refundQueryParam.setPage(
                    req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1
            );
            refundQueryParam.setPageSize(
                    req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : 10
            );
            refundQueryParam.setOrderId(
                    req.getParameter("orderId") != null ? Integer.parseInt(req.getParameter("orderId")) : null
            );
            refundQueryParam.setUserId(
                    req.getParameter("userId") != null ? Integer.parseInt(req.getParameter("userId")) : null
            );
            refundQueryParam.setStatus(
                    req.getParameter("status") != null ? Integer.parseInt(req.getParameter("status")) : null
            );
        }
        log.info("接收到的查询参数:{}", refundQueryParam);
        PageResult<Refund> pageResult = refundService.selectRefundsByPage(refundQueryParam);
        log.info("查询到的退票申请信息:{}", pageResult);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }

    // 创建退票申请
    private void createRefund(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = ServletUtils.getRequestBody(req);
        Refund refund = JsonUtils.parseJson(json, Refund.class);
        log.info("接收到的退票申请信息:{}", refund);
        try {
            refundService.createRefund(refund);
            ServletUtils.sendResponse(resp, Result.success());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
   }

   // 退票申请处理
    private void processRefund(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Integer> ids = ServletUtils.parseReqToList(req.getParameterValues("ids"), Integer.class);
        Integer adminId = Integer.parseInt(req.getParameter("adminId"));
        Integer status = Integer.parseInt(req.getParameter("status"));
        refundService.processRefunds(ids, adminId, status);
        ServletUtils.sendResponse(resp, Result.success());
   }

   //查询指定用户的所有退票申请
    private void selectRefundsByUserId(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String pathInfo = req.getPathInfo();
        Integer userId = Integer.parseInt(pathInfo.substring(1));
        List<Refund> refunds = refundService.selectRefundsByUserId(userId);
        ServletUtils.sendResponse(resp, Result.success(refunds));
    }
}
