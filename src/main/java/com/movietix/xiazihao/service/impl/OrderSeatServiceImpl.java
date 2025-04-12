package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderDaoImpl;
import com.movietix.xiazihao.dao.Impl.OrderSeatDaoImpl;
import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.dao.OrderSeatDao;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.pojo.OrderSeat;
import com.movietix.xiazihao.service.OrderSeatService;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
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
        JdbcUtils.executeTransaction(conn -> {
            try {
                for (OrderSeat orderSeat : orderSeatList) {
                    orderSeatDao.addOrderSeat(orderSeat, conn, false);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    public OrderSeat selectOrderSeatById(Integer id) throws SQLException {
        return orderSeatDao.selectOrderSeatById(id,true);
    }

    @Override
    public OrderSeat selectOrderSeatsByCondition(String orderId, String seatRow, String seatCol) throws SQLException {
        return orderSeatDao.selectOrderSeatsByCondition(orderId, seatRow, seatCol,true);
    }

    @Override
    public List<OrderSeat> selectOrderSeatsByOrderId(Integer orderId) throws SQLException {
        return orderSeatDao.selectOrderSeatsByOrderId(orderId,true);
    }

    @Override
    public void updateOrderSeats(OrderSeat orderSeat) throws Exception {
        //先检查座位是否已经被预定
        OrderSeat oldOrderSeat = orderSeatDao.selectOrderSeatById(orderSeat.getId(), true);
        Order order = orderDao.selectOrderById(oldOrderSeat.getOrderId(), true);
        Integer count = orderSeatDao.checkSeatIsReserved(order.getScreeningId(), orderSeat.getSeatRow(), orderSeat.getSeatCol(), true);
        if(count > 0) throw new Exception("座位已被预定");

        orderSeatDao.updateOrderSeats(orderSeat,true);
    }

    @Override
    public void deleteOrderSeats(List<Integer> ids) throws SQLException {
        orderSeatDao.deleteOrderSeats(ids,true);
    }
}
