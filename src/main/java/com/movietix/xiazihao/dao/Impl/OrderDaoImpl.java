package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.entity.param.OrderQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    @Override
    public Integer selectOrdersCount(OrderQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM orders o " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR o.order_no = ?) " +
                "    AND (? IS NULL OR o.user_id = ?) " +
                "    AND (? IS NULL OR o.screening_id = ?) " +
                "    AND (? IS NULL OR o.status = ?) " +
                "    AND (? IS NULL OR o.total_amount >= ?) " +
                "    AND (? IS NULL OR o.total_amount <= ?) " +
                "    AND (? IS NULL OR o.created_at >= ?) " +
                "    AND (? IS NULL OR o.created_at <= ?)";

        List<Integer> total = JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    try {
                        return rs.getInt(1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                },
                param.getOrderNo(), param.getOrderNo(),
                param.getUserId(), param.getUserId(),
                param.getScreeningId(), param.getScreeningId(),
                param.getStatus(), param.getStatus(),
                param.getMinAmount(), param.getMinAmount(),
                param.getMaxAmount(), param.getMaxAmount(),
                param.getStartDate(), param.getStartDate(),
                param.getEndDate(), param.getEndDate()
        );
        return total.isEmpty() ? 0 : total.get(0);
    }

    @Override
    public List<Order> selectOrdersByPage(OrderQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT o.* " +
                "FROM orders o " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR o.order_no = ?) " +
                "    AND (? IS NULL OR o.user_id = ?) " +
                "    AND (? IS NULL OR o.screening_id = ?) " +
                "    AND (? IS NULL OR o.status = ?) " +
                "    AND (? IS NULL OR o.total_amount >= ?) " +
                "    AND (? IS NULL OR o.total_amount <= ?) " +
                "    AND (? IS NULL OR o.created_at >= ?) " +
                "    AND (? IS NULL OR o.created_at <= ?) " +
                "ORDER BY o.created_at DESC " +
                "LIMIT ? OFFSET ?";

        return JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Order order = new Order();
                    try {
                        order.setId(rs.getInt("id"));
                        order.setOrderNo(rs.getString("order_no"));
                        order.setUserId(rs.getInt("user_id"));
                        order.setScreeningId(rs.getInt("screening_id"));
                        order.setTotalAmount(rs.getBigDecimal("total_amount"));
                        order.setSeatCount(rs.getInt("seat_count"));
                        order.setStatus(rs.getInt("status"));
                        order.setContactPhone(rs.getString("contact_phone"));
                        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return order;
                },
                param.getOrderNo(), param.getOrderNo(),
                param.getUserId(), param.getUserId(),
                param.getScreeningId(), param.getScreeningId(),
                param.getStatus(), param.getStatus(),
                param.getMinAmount(), param.getMinAmount(),
                param.getMaxAmount(), param.getMaxAmount(),
                param.getStartDate(), param.getStartDate(),
                param.getEndDate(), param.getEndDate(),
                param.getPageSize(), (param.getPage() - 1) * param.getPageSize()
        );
    }

    @Override
    public Order selectOrderById(Integer id, Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * " +
                "FROM orders " +
                "WHERE id = ?";
        List<Order> orderList = JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Order order = new Order();
                    try {
                        order.setId(rs.getInt("id"));
                        order.setOrderNo(rs.getString("order_no"));
                        order.setUserId(rs.getInt("user_id"));
                        order.setScreeningId(rs.getInt("screening_id"));
                        order.setTotalAmount(rs.getBigDecimal("total_amount"));
                        order.setSeatCount(rs.getInt("seat_count"));
                        order.setStatus(rs.getInt("status"));
                        order.setContactPhone(rs.getString("contact_phone"));
                        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return order;
                },
                id
        );
        return !orderList.isEmpty() ? orderList.get(0) : null;
    }

    @Override
    public void cancelOrder(Integer id, Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE orders " +
                "SET status = 2, updated_at = NOW() " +
                "WHERE id = ?";
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn, id);
    }

    @Override
    public void createOrder(Order order, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO orders (order_no, user_id, screening_id, total_amount, seat_count, status, contact_phone, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,
                order.getOrderNo(),
                order.getUserId(),
                order.getScreeningId(),
                order.getTotalAmount(),
                order.getSeatCount(),
                order.getStatus(),
                order.getContactPhone()
        );
    }

    @Override
    public void updateOrder(Order order, Connection conn, boolean isAutoCloseConn) throws SQLException {
      String sql = "UPDATE orders SET " +
                 "total_amount = COALESCE(?, total_amount), " +
                 "seat_count = COALESCE(?, seat_count), " +
                 "status = COALESCE(?, status), " +
                 "contact_phone = COALESCE(?, contact_phone), " +
                 "updated_at = NOW() " +
                 "WHERE id = ?";
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,
                order.getTotalAmount(),
                order.getSeatCount(),
                order.getStatus(),
                order.getContactPhone(),
                order.getId()
        );
    }

    @Override
    public Integer selectOrdersMaxId(Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT MAX(id) " +
                "FROM orders";
        List<Integer> maxId = JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    try {
                        return rs.getInt(1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return maxId.isEmpty() ? 0 : maxId.get(0);
    }
}
