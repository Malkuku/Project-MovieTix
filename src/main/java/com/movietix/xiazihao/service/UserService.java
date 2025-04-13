package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    //根据username查询用户信息
    User selectUserByUsername(String username) throws SQLException;

    // 条件分页查询用户信息
    PageResult<User> selectUsersByPage(UserQueryParam userQueryParam) throws SQLException;

    //批量删除用户信息
    void deleteUsersByIds(List<Integer> ids) throws SQLException;

    //添加用户
    void addUser(User user) throws SQLException;

    //修改用户密码
    void updateUserPassword(User user) throws SQLException;

    //修改用户余额
    void updateUserBalance(User user) throws SQLException;

    //批量修改用户状态
    void updateUserStatus(List<Integer> ids, Integer status) throws SQLException;

    //根据ID查询用户信息
    User selectUserById(Integer id);

}
