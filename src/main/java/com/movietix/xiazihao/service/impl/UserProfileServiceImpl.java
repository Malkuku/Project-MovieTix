package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.UserProfileDaoImpl;
import com.movietix.xiazihao.dao.UserProfileDao;
import com.movietix.xiazihao.entity.pojo.UserProfile;
import com.movietix.xiazihao.service.UserProfileService;
import com.movietix.xiazihao.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private static final UserProfileDao userProfileDao = new UserProfileDaoImpl();

    @Override
    public void addUserProfile(UserProfile userProfile) throws SQLException {
        userProfileDao.addUserProfile(userProfile, JdbcUtils.getConnection(),true);
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) throws SQLException {
        userProfileDao.updateUserProfile(userProfile,JdbcUtils.getConnection(),true);
    }

    @Override
    public void deleteUserProfileByIds(List<Integer> ids) throws SQLException {
        userProfileDao.deleteUserProfileByIds(ids,JdbcUtils.getConnection(),true);
    }

    @Override
    public UserProfile selectUserProfileByUserId(Integer userId) throws SQLException {
        UserProfile userProfile =  userProfileDao.selectUserProfileByUserId(userId,JdbcUtils.getConnection(),true);
        log.info("userProfile:{}",userProfile);
        return userProfile;
    }
}
