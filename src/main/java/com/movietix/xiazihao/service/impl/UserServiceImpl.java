package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.UserDaoImpl;
import com.movietix.xiazihao.dao.UserDao;
import com.movietix.xiazihao.service.UserService;

public class UserServiceImpl implements UserService {
    private static final UserDao userDao = new UserDaoImpl();

}
