package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.UserDaoImpl;
import com.movietix.xiazihao.dao.UserDao;
import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static final UserDao userDao = new UserDaoImpl();

    @Override
    public PageResult<User> selectUsersByPage(UserQueryParam userQueryParam) throws SQLException {
        Integer total = userDao.selectUsersCount(userQueryParam, true);
        List<User> users = userDao.selectUsersByPage(userQueryParam, true);
        return new PageResult<>(total, users);
    }

    @Override
    public void deleteUsersByIds(List<Integer> ids) throws SQLException {
        if(ids == null || ids.isEmpty()){
            return;
        }
        userDao.deleteUsersByIds(ids, true);
    }

    @Override
    public void addUser(User user) throws SQLException {
        userDao.addUser(user, true);
    }
}
