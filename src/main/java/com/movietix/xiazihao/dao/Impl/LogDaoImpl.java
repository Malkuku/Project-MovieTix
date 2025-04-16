package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.LogDao;
import com.movietix.xiazihao.entity.pojo.Log;
import com.movietix.xiazihao.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class LogDaoImpl implements LogDao {
    @Override
    public void deleteLogByIds(List<Integer> ids, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "DELETE FROM logs WHERE id IN (" +
                String.join(",", ids.stream().map(id -> "?").toArray(String[]::new)) +
                ")";
        log.info("Executing SQL: {}", sql);
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,ids.toArray());
    }

    @Override
    public List<Log> selectAllLogs(Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * FROM logs";
        List<Log> logList = JdbcUtils.executeQuery(conn, sql,isAutoCloseConn, rs -> {
            Log log = new Log();
            try {
                log.setId(rs.getInt("id"));
                log.setUserId(rs.getInt("user_id"));
                log.setAction(rs.getString("action"));
                log.setIpAddress(rs.getString("ip_address"));
                log.setUserAgent(rs.getString("user_agent"));
                log.setDetails(rs.getString("details"));
                log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return log;
        });
        log.info(logList.toString());
        return logList;
    }
}
