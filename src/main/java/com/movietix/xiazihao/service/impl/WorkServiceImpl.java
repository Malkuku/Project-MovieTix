package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderDaoImpl;
import com.movietix.xiazihao.dao.Impl.UserDaoImpl;
import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.dao.UserDao;
import com.movietix.xiazihao.entity.body.WorkOrderQueryBody;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.service.OrderSeatService;
import com.movietix.xiazihao.service.OrderService;
import com.movietix.xiazihao.service.UserService;
import com.movietix.xiazihao.service.WorkService;
import com.movietix.xiazihao.utils.JwtUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class WorkServiceImpl implements WorkService {
    private static final UserDao userDao = new UserDaoImpl();

    private static final UserService userService = new UserServiceImpl();
    private static final OrderService orderService = new OrderServiceImpl();
    private static final OrderSeatService orderSeatService = new OrderSeatServiceImpl();

    private static final OrderDao orderDao = new OrderDaoImpl();

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
        userService.updateUserBalance(user);
    }

    @Override
    public Integer userBuyTicket(WorkOrderQueryBody workOrderQueryBody) throws Exception {
        //先创建订单
        Order order = new Order();
        order.setUserId(workOrderQueryBody.getUserId());
        order.setScreeningId(workOrderQueryBody.getScreeningId());
        order.setContactPhone(workOrderQueryBody.getContactPhone());
        //TODO: 这个地方会在高并发时出现问题，需要加锁
        //转交给OrderServiceImpl处理
        orderService.createOrder(order);
        //获取订单ID
        Integer orderId = orderDao.selectOrdersMaxId(true);
        //补充座位信息
        for(int i = 0; i < workOrderQueryBody.getSeats().size(); i++){
            workOrderQueryBody.getSeats().get(i).setOrderId(orderId);
        }
        //添加订单座位
        orderSeatService.addOrderSeats(workOrderQueryBody.getSeats());
        return orderId;
    }
}
