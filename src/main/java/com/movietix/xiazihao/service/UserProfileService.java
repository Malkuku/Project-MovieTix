package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.pojo.UserProfile;

import java.sql.SQLException;
import java.util.List;

public interface UserProfileService {
    // 添加用户详细信息
    void addUserProfile(UserProfile userProfile) throws SQLException;

    // 修改用户详细信息
    void updateUserProfile(UserProfile userProfile) throws SQLException;

    // 删除用户详细信息
    void deleteUserProfileByIds(List<Integer> ids) throws SQLException;

    // 根据用户id查询用户详细信息
    UserProfile selectUserProfileByUserId(Integer userId) throws SQLException;
}
