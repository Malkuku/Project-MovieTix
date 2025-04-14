package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.UserProfileDaoImpl;
import com.movietix.xiazihao.dao.UserProfileDao;
import com.movietix.xiazihao.entity.pojo.UserProfile;
import com.movietix.xiazihao.service.UserProfileService;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;

public class UserProfileServiceImpl implements UserProfileService {
    private static final UserProfileDao userProfileDao = new UserProfileDaoImpl();

    @Override
    public void addUserProfile(UserProfile userProfile) throws SQLException {
        userProfileDao.addUserProfile(userProfile, JdbcUtils.getConnection(),true);
    }
}
