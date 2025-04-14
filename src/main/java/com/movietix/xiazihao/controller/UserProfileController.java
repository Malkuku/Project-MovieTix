package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.service.UserProfileService;
import com.movietix.xiazihao.service.impl.UserProfileServiceImpl;
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
    }
    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
