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
}
