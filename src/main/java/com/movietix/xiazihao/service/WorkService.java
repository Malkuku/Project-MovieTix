package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.body.WorkPaymentQueryBody;
import com.movietix.xiazihao.entity.param.WorkOrderQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.WorkOrderResult;
import com.movietix.xiazihao.entity.result.WorkSeatResult;

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
    Integer userBuyTicket(Integer screeningId,Integer userId) throws Exception;

    //用户支付操作
    void payOrder(WorkPaymentQueryBody workOrderQueryBody) throws Exception;

    //条件查询订单信息
    List<WorkOrderResult> selectWorkOrders(WorkOrderQueryParam workOrderQueryParam) throws Exception;

    //取消订单
    void cancelOrder(Integer orderId) throws SQLException;

    //查询放映场次信息的座位信息
    WorkSeatResult selectSeatsByScreeningId(Integer screeningId) throws SQLException;
}
