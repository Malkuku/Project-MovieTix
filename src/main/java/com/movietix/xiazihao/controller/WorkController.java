package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.body.WorkPaymentQueryBody;
import com.movietix.xiazihao.entity.param.WorkOrderQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.entity.result.WorkOrderResult;
import com.movietix.xiazihao.entity.result.WorkSeatResult;
import com.movietix.xiazihao.service.WorkService;
import com.movietix.xiazihao.service.impl.WorkServiceImpl;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@WebServlet("/works/*")
public class WorkController extends HttpServlet {
    private static final WorkService workService = new WorkServiceImpl();
    private static final MovieController movieController = new MovieController();
    private static final ScreeningController screeningController = new ScreeningController();
    private static final UserController userController = new UserController();
    private static final UserProfileController userProfileController = new UserProfileController();
    private static final RefundController refundController = new RefundController();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        //用户登录
        if(pathInfo != null && pathInfo.matches("/login")) {
            try {
                userLogin(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //用户注册
        else if(pathInfo != null && pathInfo.matches("/register")) {
            try {
                userRegister(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //用户充值
        else if(pathInfo != null && pathInfo.matches("/recharge")) {
            try {
                userRecharge(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //用户购票
        else if(pathInfo != null && pathInfo.matches("/orders")) {
            try {
                userBuyTicket(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //支付待付款订单
        else if(pathInfo != null && pathInfo.matches("/orders/pay")) {
            try {
                payOrder(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //订单取消
        else if(pathInfo != null && pathInfo.matches("/orders/cancel")) {
            try {
                cancelOrder(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //用户修改密码
        else if(pathInfo != null && pathInfo.matches("/users/password")) {
            try {
                updateUserPassword(req,resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //退款订单
        else if(pathInfo != null && pathInfo.matches("/refunds")) {
            try {
                refundOrder(req, resp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        //条件分页查询电影信息
        if (pathInfo != null && pathInfo.matches("/movies")) {
            try {
                selectMoviesByPage(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //条件分页查询放映场次信息
        else if(pathInfo != null && pathInfo.matches("/screenings")) {
            try {
                selectScreeningByPage(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //查询用户订单列表
        else if(pathInfo != null && pathInfo.matches("/orders")) {
            try {
                selectOrdersByPage(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //查询用户信息详情
        else if (pathInfo != null && pathInfo.matches("/user_profiles/\\d+")){
            try {
                selectUserProfileByUserId(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //查询座位信息
        else if(pathInfo != null && pathInfo.matches("/seats")){
            try {
                selectSeatsByScreeningId(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //查询退款列表
        else if(pathInfo != null && pathInfo.matches("/refunds/\\d+")) {
            try {
                selectRefundsByUserId(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.matches("/user_profiles")){
            updateUserProfile(req, resp);
        }
    }
    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    //查询放映场次信息的座位信息
    private void selectSeatsByScreeningId(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Integer screeningId = Integer.parseInt(req.getParameter("id"));
        WorkSeatResult workSeatResult = workService.selectSeatsByScreeningId(screeningId);
        log.info("查询放映场次信息的座位信息成功,结果:{}", workSeatResult);
        ServletUtils.sendResponse(resp, Result.success(workSeatResult));
    }

    //修改用户详细信息
    private void updateUserProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        userProfileController.exposeUpdateUserProfile(req, resp);
    }

    //查询用户详细信息
    private void selectUserProfileByUserId(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        userProfileController.exposeSelectUserProfileByUserId(req, resp);
    }

    //修改用户密码
    private void updateUserPassword(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException {
       userController.exposeUpdateUserPassword(req, resp);
    }

    //申请订单退款
    private void refundOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        refundController.exposeCreateRefund(req, resp);
    }

    //查询退款申请
    private void selectRefundsByUserId(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        refundController.exposeSelectRefundsByUserId(req, resp);
    }

    //查询用户订单列表
    private void selectOrdersByPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        WorkOrderQueryParam workOrderQueryParam = new WorkOrderQueryParam();
        {
            workOrderQueryParam.setId(
                    req.getParameter("id") != null ? Integer.valueOf(req.getParameter("id")) : null
            );
            workOrderQueryParam.setMovieTitle(req.getParameter("movieTitle"));
            workOrderQueryParam.setHallName(req.getParameter("hallName"));
            workOrderQueryParam.setStartTime(
                    req.getParameter("startTime") != null ? LocalDateTime.parse(req.getParameter("startTime")) : null
            );
            workOrderQueryParam.setStatus(
                    req.getParameter("status") != null ? Integer.parseInt(req.getParameter("status")) : 1
            );
        }
        List<WorkOrderResult> workOrderResultList = workService.selectWorkOrders(workOrderQueryParam);
        log.info("查询到的订单列表：{}", workOrderResultList);
        ServletUtils.sendResponse(resp, Result.success(workOrderResultList));
    }

    //订单取消
    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Integer orderId = Integer.parseInt(req.getParameter("id"));
        workService.cancelOrder(orderId);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //支付待付款订单
    private void payOrder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String json = ServletUtils.getRequestBody(req);
        WorkPaymentQueryBody workOrderQueryBody = JsonUtils.parseJson(json, WorkPaymentQueryBody.class);
        workService.payOrder(workOrderQueryBody);
        ServletUtils.sendResponse(resp,Result.success());
    }

    //用户购票操作
    private void userBuyTicket(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Integer ScreeningId = Integer.parseInt(req.getParameter("screeningId"));
        Integer userId = Integer.parseInt(req.getParameter("userId"));
        Integer orderId = workService.userBuyTicket(ScreeningId,userId);
        HashMap<String,Integer> data = new HashMap<>();
        data.put("orderId", orderId);
        ServletUtils.sendResponse(resp, Result.success(data));
    }

    //条件分页查询放映场次信息
    private void selectScreeningByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        screeningController.exposeSelectScreeningsByPage(req,resp);
    }

    //TODO获取电影详细信息

    //根据条件分页查询电影信息
    private void selectMoviesByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        movieController.exposeSelectMoviesByPage(req,resp);
    }

    //账户余额充值
    private void userRecharge(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String json = ServletUtils.getRequestBody(req);
        User user = JsonUtils.parseJson(json, User.class);
        log.info("用户充值操作，参数：{}", user);
        workService.userRecharge(user);
        log.info("用户充值成功");
        ServletUtils.sendResponse(resp, Result.success());
    }

    //用户登录操作
    private void userLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String json = ServletUtils.getRequestBody(req);
        User user = JsonUtils.parseJson(json, User.class);
        log.info("用户登录操作，参数：{}", user);
        User userDB = workService.userLogin(user);
        log.info("用户登录成功，结果：{}", userDB);
        ServletUtils.sendResponse(resp, Result.success(userDB));
    }

    //用户注册操作
    private void userRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String json = ServletUtils.getRequestBody(req);
        User user = JsonUtils.parseJson(json, User.class);
        log.info("用户注册操作，参数：{}", user);
        workService.userRegister(user);
        log.info("用户注册成功");
        ServletUtils.sendResponse(resp, Result.success());
    }
}
