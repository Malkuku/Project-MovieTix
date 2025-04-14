package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.pojo.UserProfile;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserProfileDao {
    //添加用户详细信息
    void addUserProfile(UserProfile userProfile, Connection conn, boolean isAutoCloseConn) throws SQLException;

    //修改用户详细信息
    void updateUserProfile(UserProfile userProfile, Connection connection, boolean isAutoCloseConn) throws SQLException;
}
