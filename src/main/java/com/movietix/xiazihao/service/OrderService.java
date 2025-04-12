package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;

public interface OrderService {
    // 分页查询订单
    PageResult<Order> selectOrdersByPage(OrderQueryParam param) throws SQLException;

    //根据id查询订单
    Order selectOrderById(Integer id) throws SQLException;

    // 取消订单
    void cancelOrder(Integer id) throws SQLException;

    // 创建订单
    void createOrder(Order order) throws Exception;
}
