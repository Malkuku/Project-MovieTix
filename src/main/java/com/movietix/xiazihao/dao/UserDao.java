package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    // 条件查询用户总数
    Integer selectUsersCount(UserQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException;

    // 条件分页查询用户信息
    List<User> selectUsersByPage(UserQueryParam param,Connection conn,boolean isAutoCloseConn) throws SQLException;

    // 批量删除用户信息
    void deleteUsersByIds(List<Integer> ids, Connection conn,boolean isAutoCloseConn) throws SQLException;

    // 添加用户
    void addUser(User user,Connection conn, boolean isAutoCloseConn) throws SQLException;

    // 修改用户密码
    void updateUserPassword(User user,Connection conn, boolean isAutoCloseConn) throws SQLException;

    // 修改用户余额
    void updateUserBalance(User user, Connection conn, boolean isAutoCloseConn) throws SQLException;

    // 批量修改用户状态
    void updateUserStatus(List<Integer> ids, Integer status,Connection conn, boolean isAutoCloseConn) throws SQLException;

    // 根据ID查询用户信息
    User selectUserById(Integer id,Connection conn, boolean isAutoCloseConn) throws SQLException;

    // 根据用户名查询用户信息
    User selectUserByUsername(String username,Connection conn, boolean isAutoCloseConn) throws SQLException;
}
