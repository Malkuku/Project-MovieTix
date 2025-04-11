package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.OrderSeatDao;
import com.movietix.xiazihao.entity.pojo.OrderSeat;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.math.BigDecimal;
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

    @Override
    public OrderSeat selectOrderSeatById(Integer id,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * FROM order_seats WHERE id = ?";
        List<OrderSeat> orderSeatList = JdbcUtils.executeQuery(JdbcUtils.getConnection(),sql,isAutoCloseConn,rs -> {
            try {
                OrderSeat orderSeat = new OrderSeat();
                orderSeat.setId(rs.getInt("id"));
                orderSeat.setOrderId(rs.getInt("order_id"));
                orderSeat.setSeatRow(rs.getInt("seat_row"));
                orderSeat.setSeatCol(rs.getInt("seat_col"));
                orderSeat.setSeatNo(rs.getString("seat_no"));
                orderSeat.setPrice(BigDecimal.valueOf(rs.getDouble("price")));
                return orderSeat;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, id);
        return orderSeatList.isEmpty() ? null : orderSeatList.get(0);
    }

    @Override
    public OrderSeat selectOrderSeatsByCondition(String orderId, String seatRow, String seatCol, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * FROM order_seats WHERE order_id = ? AND seat_row = ? AND seat_col = ?";
        List<OrderSeat> orderSeatList = JdbcUtils.executeQuery(JdbcUtils.getConnection(),sql,isAutoCloseConn,rs -> {
            try {
                OrderSeat orderSeat = new OrderSeat();
                orderSeat.setId(rs.getInt("id"));
                orderSeat.setOrderId(rs.getInt("order_id"));
                orderSeat.setSeatRow(rs.getInt("seat_row"));
                orderSeat.setSeatCol(rs.getInt("seat_col"));
                orderSeat.setSeatNo(rs.getString("seat_no"));
                orderSeat.setPrice(BigDecimal.valueOf(rs.getDouble("price")));
                return orderSeat;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, orderId, seatRow, seatCol);
        return orderSeatList.isEmpty() ? null : orderSeatList.get(0);
    }

    @Override
    public List<OrderSeat> selectOrderSeatsByOrderId(Integer orderId, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * FROM order_seats WHERE order_id = ?";
        return JdbcUtils.executeQuery(JdbcUtils.getConnection(),sql,isAutoCloseConn,rs -> {
            try {
                OrderSeat orderSeat = new OrderSeat();
                orderSeat.setId(rs.getInt("id"));
                orderSeat.setOrderId(rs.getInt("order_id"));
                orderSeat.setSeatRow(rs.getInt("seat_row"));
                orderSeat.setSeatCol(rs.getInt("seat_col"));
                orderSeat.setSeatNo(rs.getString("seat_no"));
                orderSeat.setPrice(BigDecimal.valueOf(rs.getDouble("price")));
                return orderSeat;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, orderId);
    }
}
