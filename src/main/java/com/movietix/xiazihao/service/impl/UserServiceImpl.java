package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.UserDaoImpl;
import com.movietix.xiazihao.dao.UserDao;
import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.UserService;
import com.movietix.xiazihao.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class UserServiceImpl implements UserService {
    private static final UserDao userDao = new UserDaoImpl();

    @Override
    public void updateUserBalance(User user) throws SQLException {
        //单次充值金额不能大于1e5
        if(user.getBalance().compareTo(new java.math.BigDecimal("100000")) > 0){
            throw new RuntimeException("单次充值金额不能大于1万");
        }
        User userFromDb = userDao.selectUserById(user.getId(),JdbcUtils.getConnection(), true);
        if(userFromDb == null){
            throw new RuntimeException("用户不存在");
        }
        user.setBalance(userFromDb.getBalance().add(user.getBalance()));
        userDao.updateUserBalance(user, JdbcUtils.getConnection(),true);
    }

    @Override
    public void updateUserStatus(List<Integer> ids, Integer status) throws SQLException {
        userDao.updateUserStatus(ids, status,JdbcUtils.getConnection(),  true);
    }

    @Override
    public User selectUserById(Integer id) {
        try {
            return userDao.selectUserById(id,JdbcUtils.getConnection(),  true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User selectUserByUsername(String username) throws SQLException {
        return userDao.selectUserByUsername(username,JdbcUtils.getConnection(),  true);
    }

    @Override
    public PageResult<User> selectUsersByPage(UserQueryParam userQueryParam) throws SQLException {
        Integer total = userDao.selectUsersCount(userQueryParam, JdbcUtils.getConnection(), true);
        List<User> users = userDao.selectUsersByPage(userQueryParam,JdbcUtils.getConnection(),  true);
        return new PageResult<>(total, users);
    }

    @Override
    public void deleteUsersByIds(List<Integer> ids) throws SQLException {
        if(ids == null || ids.isEmpty()){
            return;
        }
        userDao.deleteUsersByIds(ids,JdbcUtils.getConnection(),  true);
    }

    @Override
    public void addUser(User user) throws SQLException {
        userDao.addUser(user, JdbcUtils.getConnection(), true);
    }

    @Override
    public void updateUserPassword(User user) throws SQLException {
        //检查原密码
        User userDB = userDao.selectUserById(user.getId(),JdbcUtils.getConnection(), true);
        log.info(String.valueOf(userDB));
        if(!user.getOldPasswordHash().equals(userDB.getPasswordHash())){
            throw new RuntimeException("原密码不匹配");
        }
        userDao.updateUserPassword(user,JdbcUtils.getConnection(),  true);
        //TODO 将token过期然后要求重新登录？
    }
}
