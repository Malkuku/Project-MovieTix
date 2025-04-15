package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.*;
import com.movietix.xiazihao.dao.Impl.*;
import com.movietix.xiazihao.entity.body.WorkOrderQueryBody;
import com.movietix.xiazihao.entity.param.WorkOrderQueryParam;
import com.movietix.xiazihao.entity.pojo.*;
import com.movietix.xiazihao.entity.result.WorkOrderResult;
import com.movietix.xiazihao.service.*;
import com.movietix.xiazihao.utils.JdbcUtils;
import com.movietix.xiazihao.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WorkServiceImpl implements WorkService {
    private static final UserService userService = new UserServiceImpl();
    private static final OrderService orderService = new OrderServiceImpl();
    private static final OrderSeatService orderSeatService = new OrderSeatServiceImpl();
    private static final PaymentService paymentService = new PaymentServiceImpl();

    private static final WorkDao workDao = new WorkDaoImpl();
    private static final UserDao userDao = new UserDaoImpl();
    private static final OrderDao orderDao = new OrderDaoImpl();
    private static final PaymentDao paymentDao = new PaymentDaoImpl();
    private static final UserProfileDao userProfileDao = new UserProfileDaoImpl();

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
        //如果是管理员，生成管理员token
        if(userFromDb.getIsAdmin() == 1){
            String adminToken = JwtUtils.createAdminToken(claims);
            userFromDb.setAdminToken(adminToken);
        }
        return userFromDb;
    }

    @Override
    public void userRegister(User user) throws SQLException {
        //检查用户名是否已存在
        User userFromDb = userDao.selectUserByUsername(user.getUsername(),true);
        if(userFromDb != null){
            throw new RuntimeException("用户名已存在");
        }
        //TODO lock
        //添加用户
        userDao.addUser(user, true);
        //添加用户详细信息表
        UserProfile userProfile = new UserProfile();
        userFromDb = userDao.selectUserByUsername(user.getUsername(),true);
        userProfile.setUserId(userFromDb.getId());
        userProfileDao.addUserProfile(userProfile, JdbcUtils.getConnection(),true);
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

    @Override
    public void payOrder(Integer orderId, Integer userId) throws Exception {
        //获取订单信息
        Order order = orderService.selectOrderById(orderId);
        if(order == null){
            throw new RuntimeException("订单不存在");
        }
        if(order.getStatus() != 0){
            throw new RuntimeException("订单状态不正确");
        }
        //创建支付记录表 //TODO 需要加锁
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getTotalAmount());
        paymentService.addPayment(payment);
        payment.setId(paymentDao.selectPaymentsMaxId(true));
        //检查用户余额是否足够
        User user = userService.selectUserById(userId);
        if(user.getBalance().compareTo(order.getTotalAmount()) < 0){
            //更新支付记录状态
            payment.setStatus(2);
            paymentDao.updatePayment(payment, JdbcUtils.getConnection(),true);
            throw new RuntimeException("余额不足");
        }
        //扣除用户余额->更新支付记录状态->更新订单状态
        JdbcUtils.executeTransaction(conn->{
            try {
                user.setBalance(user.getBalance().subtract(order.getTotalAmount()));
                payment.setStatus(1);
                payment.setPayTime(LocalDateTime.now());
                payment.setTransactionId("模拟支付No114514");
                payment.setPaymentMethod("余额支付");
                order.setStatus(1);
                userDao.updateUserBalance(user, conn,false);
                paymentDao.updatePayment(payment, conn,false);
                orderDao.updateOrder(order, conn,false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    public List<WorkOrderResult> selectWorkOrders(WorkOrderQueryParam workOrderQueryParam) throws Exception {
        List<WorkOrderResult> workOrderResultList = workDao.selectWorkOrders(workOrderQueryParam,true);
        //查询seatList
        for (WorkOrderResult workOrderResult : workOrderResultList) {
            workOrderResult.setSeats(orderSeatService.selectOrderSeatsByOrderId(workOrderResult.getOrderId()));
        }
        log.info("查询到的订单列表:{}", workOrderResultList);
        return workOrderResultList;
    }

    @Override
    public void cancelOrder(Integer orderId) throws SQLException {
        orderService.cancelOrder(orderId);
    }

    @Override
    public List<OrderSeat> selectSeatsByScreeningId(Integer screeningId) throws SQLException {
        return workDao.selectSeatsByScreeningId(screeningId,JdbcUtils.getConnection(),true);
    }
}
