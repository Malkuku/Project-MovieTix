package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderDaoImpl;
import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.OrderService;

import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private static final OrderDao orderDao = new OrderDaoImpl();

    @Override
    public PageResult<Order> selectOrdersByPage(OrderQueryParam param) throws SQLException {
        Integer total = orderDao.selectOrdersCount(param,true);
        List<Order> list = orderDao.selectOrdersByPage(param,true);
        return new PageResult<>(total,list);
    }

    @Override
    public Order selectOrderById(Integer id) throws SQLException {
        return orderDao.selectOrderById(id,true);
    }

    @Override
    public void cancelOrder(Integer id) throws SQLException {
        //先检查是否为待支付状态
        Order order = orderDao.selectOrderById(id,true);
        if(order.getStatus() != 0){
            throw new RuntimeException("订单状态不正确，无法取消");
        }
        orderDao.cancelOrder(id,true);
    }
}
