package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.pojo.UserProfile;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserProfileService {
    // 添加用户详细信息
    void addUserProfile(UserProfile userProfile) throws SQLException;
}
