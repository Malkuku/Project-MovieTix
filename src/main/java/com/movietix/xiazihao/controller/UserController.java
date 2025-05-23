package com.movietix.xiazihao.controller;

import com.movietix.xiazihao.constants.ConstantsManager;
import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.entity.result.Result;
import com.movietix.xiazihao.service.UserService;
import com.movietix.xiazihao.service.impl.UserServiceImpl;
import com.movietix.xiazihao.utils.JsonUtils;
import com.movietix.xiazihao.utils.JwtUtils;
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
import java.util.List;

@Slf4j
@WebServlet("/users/*")
public class UserController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    //post请求入口
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            addUser(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get请求入口
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            selectUserById(req, resp);
        }else{
            try {
                selectUsersByPage(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //put请求入口
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo != null && pathInfo.matches("/password")){
            try {
                updateUserPassword(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(pathInfo != null && pathInfo.matches("/balance")){
            try {
                updateUserBalance(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(pathInfo != null && pathInfo.matches("/status")){
            try {
                updateUserStatus(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    //delete请求入口
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            deleteUsersByIds(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //修改用户状态
    private void updateUserStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        List<Integer> ids = ServletUtils.parseReqToList(req.getParameterValues("ids"), Integer.class);
        Integer status = Integer.parseInt(req.getParameter("status"));
        userService.updateUserStatus(ids, status);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //根据ID查询用户基础信息
    private void selectUserById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String idStr = pathInfo.substring(1); // 去掉开头的"/"
        Integer id = Integer.parseInt(idStr);
        log.info("接收到的用户ID:{}", id);
        User user = userService.selectUserById(id);
        log.info("查询到的用户信息:{}", user);
        ServletUtils.sendResponse(resp, Result.success(user));
    }

    //修改用户余额
    private void updateUserBalance(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String json = ServletUtils.getRequestBody(req);
        User user = JsonUtils.parseJson(json, User.class);
        log.info("接收到的用户信息:{}", user);
        userService.updateUserBalance(user);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //修改用户密码
    private void updateUserPassword(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String json = ServletUtils.getRequestBody(req);
        User user = JsonUtils.parseJson(json, User.class);
        //验证userId
        if(!ConstantsManager.getInstance().getFilterSwitch()){
            String token = req.getHeader("token");
            JwtUtils.verifyUserId(token,user.getId());
        }
        log.info("接收到的用户信息:{}", user);
        userService.updateUserPassword(user);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //添加用户
    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String json = ServletUtils.getRequestBody(req);
        User user = JsonUtils.parseJson(json, User.class);
        log.info("接收到的用户信息:{}", user);
        userService.addUser(user);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //批量删除用户
    private void deleteUsersByIds(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String[] values = req.getParameterValues("ids");
        List<Integer> ids = ServletUtils.parseReqToList(values,Integer.class);
        log.info("接收到的用户删除参数:{}", ids);
        userService.deleteUsersByIds(ids);
        ServletUtils.sendResponse(resp, Result.success());
    }

    //条件分页查询用户信息
    private void selectUsersByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        UserQueryParam userQueryParam = new UserQueryParam();
        {
            userQueryParam.setUsername(req.getParameter("username"));
            userQueryParam.setIsAdmin(
                    req.getParameter("isAdmin") != null ? Integer.parseInt(req.getParameter("isAdmin")) : null
            );
            userQueryParam.setIsBlocked(
                    req.getParameter("isBlocked") != null ? Integer.parseInt(req.getParameter("isBlocked")) : null
            );
            userQueryParam.setCreatedAtFrom(
                    req.getParameter("createdAtFrom") != null ? LocalDateTime.parse(req.getParameter("createdAtFrom")) : null
            );
            userQueryParam.setCreatedAtTo(
                    req.getParameter("createdAtTo") != null ? LocalDateTime.parse(req.getParameter("createdAtTo")) : null
            );
            userQueryParam.setPage(
                    req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : userQueryParam.getPage()
            );
            userQueryParam.setPageSize(
                    req.getParameter("pageSize") != null ? Integer.parseInt(req.getParameter("pageSize")) : userQueryParam.getPageSize()
            );
        }
        log.info("接收到的用户查询参数:{}", userQueryParam);
        PageResult<User> pageResult = userService.selectUsersByPage(userQueryParam);
        ServletUtils.sendResponse(resp, Result.success(pageResult));
    }

    //暴露方法
    public void exposeUpdateUserPassword(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.updateUserPassword(req, resp);
    }
}
