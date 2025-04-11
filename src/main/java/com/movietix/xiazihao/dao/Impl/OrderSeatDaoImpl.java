package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.OrderSeatDao;
import com.movietix.xiazihao.entity.pojo.OrderSeat;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class OrderSeatDaoImpl implements OrderSeatDao {
    @Override
    public void addOrderSeat(List<OrderSeat> orderSeatList, boolean isAutoCloseConn) throws Exception {
        String sql = "INSERT INTO order_seats (order_id, seat_row, seat_col, seat_no, price) VALUES (?, ?, ?, ?, ?)";
        JdbcUtils.executeTransaction(conn->{
            for(OrderSeat orderSeat : orderSeatList) {
                try {
                    JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,orderSeat.getOrderId(), orderSeat.getSeatRow(), orderSeat.getSeatCol(), orderSeat.getSeatNo(), orderSeat.getPrice());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
    }

    @Override
    public Integer checkSeatIsReserved(Integer screeningId, Integer seatRow, Integer seatCol,boolean isAutoCloseConn) throws SQLException {
        String sql =  "SELECT COUNT(*) \n" +
                "FROM order_seats os\n" +
                "JOIN orders o ON os.order_id = o.id\n" +
                "WHERE o.screening_id = ?         \n" +
                "  AND o.status IN (0, 1, 3)      \n" +
                "  AND os.seat_row = ?              \n" +
                "  AND os.seat_col = ?;            ";
        List<Integer> countList = JdbcUtils.executeQuery(JdbcUtils.getConnection(),sql,isAutoCloseConn,rs -> {
            try {
                return rs.getInt(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, screeningId,seatRow,seatCol);
        return countList.isEmpty() ? 0 : countList.get(0);
    }
}
