package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.UserDaoImpl;
import com.movietix.xiazihao.dao.UserDao;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.service.WorkService;
import com.movietix.xiazihao.utils.JwtUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class WorkServiceImpl implements WorkService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public User userLogin(User user) throws SQLException {
        //检查用户名和密码是否正确
        User userFromDb = userDao.selectUserByUsername(user.getUsername(),true);
        if(userFromDb == null){
            throw new RuntimeException("用户不存在");
        }if(userFromDb.getIsBlocked() == 1){
            throw new RuntimeException("用户已被封禁");
        }if(!userFromDb.getPasswordHash().equals(user.getPasswordHash())){
            throw new RuntimeException("密码错误");
        }
        //登录成功，生成token，返回用户信息
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userFromDb.getId());
        claims.put("username", userFromDb.getUsername());
        String token = JwtUtils.createToken(claims);
        userFromDb.setToken(token);
        return userFromDb;
    }

    @Override
    public void userRegister(User user) throws SQLException {
        //检查用户名是否已存在
        User userFromDb = userDao.selectUserByUsername(user.getUsername(),true);
        if(userFromDb != null){
            throw new RuntimeException("用户名已存在");
        }
        //添加用户
        userDao.addUser(user, true);
    }

    @Override
    public void userRecharge(User user) throws SQLException {
        //单次充值金额不能大于1e5
        if(user.getBalance().compareTo(new java.math.BigDecimal("100000")) > 0){
            throw new RuntimeException("单次充值金额不能大于1万");
        }
        User userFromDb = userDao.selectUserById(user.getId(),true);
        if(userFromDb == null){
            throw new RuntimeException("用户不存在");
        }
        user.setBalance(userFromDb.getBalance().add(user.getBalance()));
        userDao.updateUserBalance(user, true);
    }
}
