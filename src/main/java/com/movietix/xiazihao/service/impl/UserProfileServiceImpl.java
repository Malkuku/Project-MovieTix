package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.UserProfileDaoImpl;
import com.movietix.xiazihao.dao.UserProfileDao;
import com.movietix.xiazihao.service.UserProfileService;

public class UserProfileServiceImpl implements UserProfileService {
    private static final UserProfileDao userProfileDao = new UserProfileDaoImpl();
}
