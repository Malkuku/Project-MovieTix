package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderDaoImpl;
import com.movietix.xiazihao.dao.Impl.OrderSeatDaoImpl;
import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.dao.OrderSeatDao;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.pojo.OrderSeat;
import com.movietix.xiazihao.service.OrderSeatService;

import java.util.List;

public class OrderSeatServiceImpl implements OrderSeatService {
    private final OrderSeatDao orderSeatDao = new OrderSeatDaoImpl();
    private final OrderDao orderDao = new OrderDaoImpl();

    @Override
    public void addOrderSeats(List<OrderSeat> orderSeatList) throws Exception {
        //先检查座位是否已经被预定
        for (OrderSeat orderSeat : orderSeatList) {
            Order order = orderDao.selectOrderById(orderSeat.getOrderId(), true);
            Integer count = orderSeatDao.checkSeatIsReserved(order.getScreeningId(), orderSeat.getSeatRow(), orderSeat.getSeatCol(), true);
            if(count > 0){
                throw new Exception("座位已被预定");
            }
        }
        orderSeatDao.addOrderSeat(orderSeatList,false);
    }
}
