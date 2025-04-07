package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;

public interface UserService {
    // 条件分页查询用户信息
    PageResult<User> selectUsersByPage(UserQueryParam userQueryParam) throws SQLException;
}
