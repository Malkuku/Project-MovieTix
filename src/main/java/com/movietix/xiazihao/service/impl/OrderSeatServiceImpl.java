package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderSeatDaoImpl;
import com.movietix.xiazihao.dao.OrderSeatDao;
import com.movietix.xiazihao.service.OrderSeatService;

public class OrderSeatServiceImpl implements OrderSeatService {
    private final OrderSeatDao orderSeatDao = new OrderSeatDaoImpl();
}
