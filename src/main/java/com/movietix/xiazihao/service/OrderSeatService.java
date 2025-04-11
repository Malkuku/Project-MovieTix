package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.pojo.OrderSeat;

import java.sql.SQLException;
import java.util.List;

public interface OrderSeatService {
    //批量添加座位
    void addOrderSeats(List<OrderSeat> orderSeatList) throws Exception;

    //根据id查询座位
    OrderSeat selectOrderSeatById(Integer id) throws SQLException;
}
