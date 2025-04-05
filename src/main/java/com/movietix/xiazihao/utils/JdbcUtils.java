package com.movietix.xiazihao.utils;

import com.movietix.xiazihao.config.DataSourceConfig;
import com.movietix.xiazihao.constants.ConstantsManager;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class JdbcUtils {
    private static final DataSource dataSource = DataSourceConfig.createDataSource();

    // 获取连接(从连接池)
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 通用查询方法 (动态SQL+参数)
    public static <T> List<T> executeQuery(String sql,
                                           Function<ResultSet, T> rowMapper,
                                           Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<T> result = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            rs = stmt.executeQuery();

            while (rs.next()) {
                log.debug("id:{}",rs.getString(1));
                result.add(rowMapper.apply(rs));
            }
            return result;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // 通用更新方法 (INSERT/UPDATE/DELETE)
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, params);
            return stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    // 设置参数
    private static void setParameters(PreparedStatement stmt, Object... params)
            throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    // 关闭资源
    private static void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 事务执行模板
    public static <T> T executeTransaction(TransactionCallback<T> action) throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            T result = action.doInTransaction(conn);
            conn.commit();
            return result;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // 事务回调接口
    public interface TransactionCallback<T> {
        T doInTransaction(Connection conn) throws SQLException;
    }
}

