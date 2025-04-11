package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    // 查询订单数量
    Integer selectOrdersCount(OrderQueryParam param,boolean isAutoCloseConn) throws SQLException;
    // 分页查询订单
    List<Order> selectOrdersByPage(OrderQueryParam param,boolean isAutoCloseConn) throws SQLException;
    //根据id查询订单
    Order selectOrderById(Integer id, boolean isAutoCloseConn) throws SQLException;
}
