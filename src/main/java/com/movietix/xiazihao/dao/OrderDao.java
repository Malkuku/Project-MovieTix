package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    // 查询订单数量
    Integer selectOrdersCount(OrderQueryParam param,boolean isAutoCloseConn) throws SQLException;
    // 分页查询订单
    List<Order> selectOrdersByPage(OrderQueryParam param,boolean isAutoCloseConn) throws SQLException;
    //根据id查询订单
    Order selectOrderById(Integer id, boolean isAutoCloseConn) throws SQLException;
    // 取消订单
    void cancelOrder(Integer id, boolean isAutoCloseConn) throws SQLException;
    // 创建订单
    void createOrder(Order order, Connection conn, boolean isAutoCloseConn) throws SQLException;
    //更新订单
    void updateOrder(Order order, Connection conn, boolean isAutoCloseConn) throws SQLException;
    //查询最大的订单id
    Integer selectOrdersMaxId(boolean isAutoCloseConn) throws SQLException;
}
