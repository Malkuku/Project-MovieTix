package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    // 条件查询用户总数
    Integer selectUsersCount(UserQueryParam param,boolean isAutoCloseConn) throws SQLException;

    // 条件分页查询用户信息
    List<User> selectUsersByPage(UserQueryParam param,boolean isAutoCloseConn) throws SQLException;

    // 批量删除用户信息
    void deleteUsersByIds(List<Integer> ids, boolean isAutoCloseConn) throws SQLException;

    // 添加用户
    void addUser(User user, boolean isAutoCloseConn) throws SQLException;

    // 修改用户密码
    void updateUserPassword(User user, boolean isAutoCloseConn) throws SQLException;
}
