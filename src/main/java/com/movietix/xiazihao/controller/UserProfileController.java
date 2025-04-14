package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.entity.pojo.UserProfile;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.UserProfileService;
import com.movietix.xiazihao.service.impl.UserProfileServiceImpl;
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
@WebServlet("/user_profiles/*")
public class UserProfileController extends HttpServlet {
    private static final UserProfileService userProfileService = new UserProfileServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            addUserProfile(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updateUserProfile(req, resp);
    }
    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    //修改用户信息
    private void updateUserProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = ServletUtils.getRequestBody(req);
        UserProfile userProfile = JsonUtils.parseJson(json, UserProfile.class);
        try {
            userProfileService.updateUserProfile(userProfile);
            ServletUtils.sendResponse(resp, Result.success());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //添加用户信息
    private void addUserProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String json = ServletUtils.getRequestBody(req);
        UserProfile userProfile = JsonUtils.parseJson(json, UserProfile.class);
        log.info("接收到的用户信息:{}", userProfile);
        userProfileService.addUserProfile(userProfile);
        ServletUtils.sendResponse(resp, Result.success());
    }
}
