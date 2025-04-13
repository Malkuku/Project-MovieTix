package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.WorkDao;
import com.movietix.xiazihao.entity.param.WorkOrderQueryParam;
import com.movietix.xiazihao.entity.result.WorkOrderResult;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class WorkDaoImpl implements WorkDao {

    @Override
    public List<WorkOrderResult> selectWorkOrders(WorkOrderQueryParam param, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT o.id AS order_id, o.order_no, m.title AS movie_title, h.name AS hall_name, " +
                "s.start_time, o.status, o.contact_phone, o.total_amount " +
                "FROM orders o " +
                "JOIN screenings s ON o.screening_id = s.id " +
                "JOIN movies m ON s.movie_id = m.id " +
                "JOIN halls h ON s.hall_id = h.id " +
                "where o.user_id = ? "+
                "    AND (? IS NULL OR m.title LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR h.name LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR s.start_time >= ?) " +
                "    AND (? IS NULL OR o.status = ?) " +
                "ORDER BY s.start_time DESC ";

        return JdbcUtils.executeQuery(
                JdbcUtils.getConnection(),
                sql,
                isAutoCloseConn,
                rs -> {
                    WorkOrderResult result = new WorkOrderResult();
                    try {
                        result.setOrderNo(rs.getString("order_no"));
                        result.setOrderId(rs.getInt("order_id"));
                        result.setMovieTitle(rs.getString("movie_title"));
                        result.setHallName(rs.getString("hall_name"));
                        result.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                        result.setStatus(rs.getInt("status"));
                        result.setContactPhone(rs.getString("contact_phone"));
                        result.setTotalAmount(rs.getBigDecimal("total_amount"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return result;
                },
                param.getId(),
                param.getMovieTitle(), param.getMovieTitle(),
                param.getHallName(), param.getHallName(),
                param.getStartTime(), param.getStartTime(),
                param.getStatus(), param.getStatus()

        );
    }

}
