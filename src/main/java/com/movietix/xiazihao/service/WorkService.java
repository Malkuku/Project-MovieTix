package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.pojo.User;

import java.sql.SQLException;

public interface WorkService {
    //用户登录操作
    User userLogin(User user) throws SQLException;

    //用户注册操作
    void userRegister(User user) throws SQLException;

    //用户充值操作
    void userRecharge(User user) throws SQLException;
}
