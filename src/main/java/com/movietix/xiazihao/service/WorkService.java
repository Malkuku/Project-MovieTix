package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.body.WorkOrderQueryBody;
import com.movietix.xiazihao.entity.param.WorkOrderQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.WorkOrderResult;

import java.sql.SQLException;
import java.util.List;

public interface WorkService {
    //用户登录操作
    User userLogin(User user) throws SQLException;

    //用户注册操作
    void userRegister(User user) throws SQLException;

    //用户充值操作
    void userRecharge(User user) throws SQLException;

    //用户购票操作
    Integer userBuyTicket(WorkOrderQueryBody workOrderQueryBody) throws Exception;

    //用户支付操作
    void payOrder(Integer orderId, Integer userId) throws Exception;

    //条件查询订单信息
    List<WorkOrderResult> selectWorkOrders(WorkOrderQueryParam workOrderQueryParam) throws Exception;

    //取消订单
    void cancelOrder(Integer orderId) throws SQLException;
}
