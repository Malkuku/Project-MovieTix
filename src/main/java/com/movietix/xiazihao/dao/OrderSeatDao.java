package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.pojo.OrderSeat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderSeatDao {
    //批量添加座位
    void addOrderSeat(OrderSeat orderSeat, Connection conn, boolean isAutoCloseConn) throws Exception;

    //检查座位是否已经被预定
    Integer checkSeatIsReserved(Integer screeningId, Integer seatRow, Integer seatCol,Connection conn,boolean isAutoCloseConn) throws Exception;

    //根据id查询座位
    OrderSeat selectOrderSeatById(Integer id,Connection conn,boolean isAutoCloseConn) throws SQLException;

    //根据复合条件查询座位
    OrderSeat selectOrderSeatsByCondition(String orderId, String seatRow, String seatCol,Connection conn, boolean isAutoCloseConn) throws SQLException;

    //根据订单id查询座位
    List<OrderSeat> selectOrderSeatsByOrderId(Integer orderId,Connection conn, boolean isAutoCloseConn) throws SQLException;

    //更新座位
    void updateOrderSeats(OrderSeat orderSeat,Connection conn, boolean isAutoCloseConn) throws SQLException;

    //批量删除座位信息
    void deleteOrderSeats(List<Integer> ids,Connection conn, boolean isAutoCloseConn) throws SQLException;
}
