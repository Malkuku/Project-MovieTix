package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.PaymentDao;
import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PaymentDaoImpl implements PaymentDao {
    @Override
    public Integer selectPaymentsCount(PaymentQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM payments " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR order_id = ?) " +
                "    AND (? IS NULL OR transaction_id = ?) " +
                "    AND (? IS NULL OR payment_method = ?) " +
                "    AND (? IS NULL OR status = ?) " +
                "    AND (? IS NULL OR pay_time >= ?) " +
                "    AND (? IS NULL OR pay_time <= ?) " +
                "    AND (? IS NULL OR amount >= ?) " +
                "    AND (? IS NULL OR amount <= ?)";

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
                param.getOrderId(), param.getOrderId(),
                param.getTransactionId(), param.getTransactionId(),
                param.getPaymentMethod(), param.getPaymentMethod(),
                param.getStatus(), param.getStatus(),
                param.getStartDate(), param.getStartDate(),
                param.getEndDate(), param.getEndDate(),
                param.getMinAmount(), param.getMinAmount(),
                param.getMaxAmount(), param.getMaxAmount()
        );
        return total.isEmpty() ? 0 : total.get(0);
    }

    @Override
    public List<Payment> selectPaymentsByPage(PaymentQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * " +
                "FROM payments " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR order_id = ?) " +
                "    AND (? IS NULL OR transaction_id = ?) " +
                "    AND (? IS NULL OR payment_method = ?) " +
                "    AND (? IS NULL OR status = ?) " +
                "    AND (? IS NULL OR pay_time >= ?) " +
                "    AND (? IS NULL OR pay_time <= ?) " +
                "    AND (? IS NULL OR amount >= ?) " +
                "    AND (? IS NULL OR amount <= ?) " +
                "ORDER BY pay_time DESC " +
                "LIMIT ? OFFSET ?";

        return JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Payment payment = new Payment();
                    try {
                        payment.setId(rs.getInt("id"));
                        payment.setOrderId(rs.getInt("order_id"));
                        payment.setAmount(rs.getDouble("amount"));
                        payment.setPaymentMethod(rs.getString("payment_method"));
                        payment.setTransactionId(rs.getString("transaction_id"));
                        payment.setStatus(rs.getInt("status"));
                        payment.setPayTime(rs.getTimestamp("pay_time") != null ?
                                rs.getTimestamp("pay_time").toLocalDateTime() : null);
                        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return payment;
                },
                param.getOrderId(), param.getOrderId(),
                param.getTransactionId(), param.getTransactionId(),
                param.getPaymentMethod(), param.getPaymentMethod(),
                param.getStatus(), param.getStatus(),
                param.getStartDate(), param.getStartDate(),
                param.getEndDate(), param.getEndDate(),
                param.getMinAmount(), param.getMinAmount(),
                param.getMaxAmount(), param.getMaxAmount(),
                param.getPageSize(), (param.getPage() - 1) * param.getPageSize()
        );
    }

    @Override
    public void addPayment(Payment payment,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO payments (order_id, amount, payment_method, transaction_id, status, pay_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        JdbcUtils.executeUpdate(
                conn,
                sql,
                isAutoCloseConn,
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getStatus(),
                payment.getPayTime()
        );
    }

    @Override
    public void updatePayment(Payment payment, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE payments SET " +
                    "order_id = COALESCE(?, order_id), " +
                    "amount = COALESCE(?, amount), " +
                    "payment_method = COALESCE(?, payment_method), " +
                    "transaction_id = COALESCE(?, transaction_id), " +
                    "status = COALESCE(?, status), " +
                    "pay_time = COALESCE(?, pay_time) " +
                    "WHERE id = ?";
        JdbcUtils.executeUpdate(
                conn,
                sql,
                isAutoCloseConn,
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getTransactionId(),
                payment.getStatus(),
                payment.getPayTime(),
                payment.getId()
        );
    }

    @Override
    public Payment selectPaymentById(Integer id,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * " +
                "FROM payments " +
                "WHERE id = ?";
        List<Payment> payments = JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Payment payment = new Payment();
                    try {
                        payment.setId(rs.getInt("id"));
                        payment.setOrderId(rs.getInt("order_id"));
                        payment.setAmount(rs.getDouble("amount"));
                        payment.setPaymentMethod(rs.getString("payment_method"));
                        payment.setTransactionId(rs.getString("transaction_id"));
                        payment.setStatus(rs.getInt("status"));
                        payment.setPayTime(rs.getTimestamp("pay_time") != null ?
                                rs.getTimestamp("pay_time").toLocalDateTime() : null);
                        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return payment;
                },
                id
        );
        return payments.isEmpty() ? null : payments.get(0);
    }

    @Override
    public Integer selectPaymentsMaxId(Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT MAX(id) " +
                "FROM payments";
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
