package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.pojo.OrderSeat;

import java.sql.SQLException;
import java.util.List;

public interface OrderSeatDao {
    //批量添加座位
    void addOrderSeat(List<OrderSeat> orderSeatList,boolean isAutoCloseConn) throws Exception;

    //检查座位是否已经被预定
    Integer checkSeatIsReserved(Integer screeningId, Integer seatRow, Integer seatCol,boolean isAutoCloseConn) throws Exception;

    //根据id查询座位
    OrderSeat selectOrderSeatById(Integer id,boolean isAutoCloseConn) throws SQLException;
}
