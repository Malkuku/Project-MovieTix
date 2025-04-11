package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;

public interface OrderService {
    // 分页查询订单
    PageResult<Order> selectOrdersByPage(OrderQueryParam param) throws SQLException;
}
