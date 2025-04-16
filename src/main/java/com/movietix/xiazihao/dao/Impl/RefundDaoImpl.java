package com.movietix.xiazihao.dao.Impl;


import com.movietix.xiazihao.dao.RefundDao;
import com.movietix.xiazihao.entity.param.RefundQueryParam;
import com.movietix.xiazihao.entity.pojo.Refund;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RefundDaoImpl implements RefundDao {

    @Override
    public Integer selectRefundsCount(RefundQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM refunds " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR order_id = ?) " +
                "    AND (? IS NULL OR user_id = ?) " +
                "    AND (? IS NULL OR status = ?)";

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
                param.getUserId(), param.getUserId(),
                param.getStatus(), param.getStatus()
        );
        return total.isEmpty() ? 0 : total.get(0);
    }

    @Override
    public List<Refund> selectRefundsByPage(RefundQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * " +
                "FROM refunds " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR order_id = ?) " +
                "    AND (? IS NULL OR user_id = ?) " +
                "    AND (? IS NULL OR status = ?) " +
                "ORDER BY created_at DESC " +
                "LIMIT ? OFFSET ?";

        return JdbcUtils.executeQuery(
                JdbcUtils.getConnection(),
                sql,
                isAutoCloseConn,
                rs -> {
                    Refund refund = new Refund();
                    try {
                        refund.setId(rs.getInt("id"));
                        refund.setOrderId(rs.getInt("order_id"));
                        refund.setUserId(rs.getInt("user_id"));
                        refund.setReason(rs.getString("reason"));
                        refund.setStatus(rs.getInt("status"));
                        refund.setAdminId(rs.getObject("admin_id") == null ? null : rs.getInt("admin_id"));
                        refund.setProcessedAt(rs.getObject("processed_at") == null ? null : rs.getTimestamp("processed_at").toLocalDateTime());
                        refund.setRefundAmount(rs.getBigDecimal("refund_amount"));
                        refund.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        refund.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return refund;
                },
                param.getOrderId(), param.getOrderId(),
                param.getUserId(), param.getUserId(),
                param.getStatus(), param.getStatus(),
                param.getPageSize(), (param.getPage() - 1) * param.getPageSize()
        );
    }

    @Override
    public void createRefund(Refund refund, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO refunds(order_id, user_id, reason, status, created_at, updated_at) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        JdbcUtils.executeUpdate(
                conn,
                sql,
                isAutoCloseConn,
                refund.getOrderId(),
                refund.getUserId(),
                refund.getReason(),
                refund.getStatus(),
                refund.getCreatedAt(),
                refund.getUpdatedAt()
        );
    }

}
