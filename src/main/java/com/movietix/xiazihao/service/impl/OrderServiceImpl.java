package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderDaoImpl;
import com.movietix.xiazihao.dao.Impl.OrderSeatDaoImpl;
import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.dao.OrderSeatDao;
import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.pojo.OrderSeat;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.OrderService;
import com.movietix.xiazihao.utils.JdbcUtils;
import com.movietix.xiazihao.utils.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao = new OrderDaoImpl();
    private final OrderSeatDao orderSeatDao = new OrderSeatDaoImpl();

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

    @Override
    public void createOrder(Order order) throws Exception {
        //预创建OrderNo
        Integer count = orderDao.selectOrdersCount(new OrderQueryParam(),true);
        String orderNo = OrderNoUtils.generateOrderNo(count);
        order.setOrderNo(orderNo);
        orderDao.createOrder(order, JdbcUtils.getConnection(),true);
    }
}
