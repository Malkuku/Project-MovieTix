package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.PaymentDao;
import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class PaymentDaoImpl implements PaymentDao {
    @Override
    public Integer selectPaymentsCount(PaymentQueryParam param, boolean isAutoCloseConn) throws SQLException {
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
                JdbcUtils.getConnection(),
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
    public List<Payment> selectPaymentsByPage(PaymentQueryParam param, boolean isAutoCloseConn) throws SQLException {
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
                JdbcUtils.getConnection(),
                sql,
                isAutoCloseConn,
                rs -> {
                    Payment payment = new Payment();
                    try {
                        payment.setId(rs.getInt("id"));
                        payment.setOrderId(rs.getInt("order_id"));
                        payment.setAmount(rs.getBigDecimal("amount"));
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
}
