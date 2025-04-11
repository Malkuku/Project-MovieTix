package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderDaoImpl;
import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.service.OrderService;

public class OrderServiceImpl implements OrderService {
    private static final OrderDao orderDao = new OrderDaoImpl();
}
