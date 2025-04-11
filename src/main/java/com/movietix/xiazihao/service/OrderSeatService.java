package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.pojo.OrderSeat;

import java.util.List;

public interface OrderSeatService {
    //批量添加座位
    void addOrderSeats(List<OrderSeat> orderSeatList) throws Exception;
}
