package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.UserService;
import com.movietix.xiazihao.service.WorkService;
import com.movietix.xiazihao.service.impl.UserServiceImpl;
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

@Slf4j
@WebServlet("/works/*")
public class WorkController extends HttpServlet {
    private final WorkService workService = new WorkServiceImpl();

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
    }
    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {

        }else{

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
