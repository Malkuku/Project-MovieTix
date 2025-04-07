package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.UserDao;
import com.movietix.xiazihao.entity.param.UserQueryParam;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public Integer selectUsersCount(UserQueryParam param, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM users " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR username LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR is_admin = ?) " +
                "    AND (? IS NULL OR is_blocked = ?) " +
                "    AND (? IS NULL OR created_at >= ?) " +
                "    AND (? IS NULL OR created_at <= ?)";

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
                param.getUsername(), param.getUsername(),
                param.getIsAdmin(), param.getIsAdmin(),
                param.getIsBlocked(), param.getIsBlocked(),
                param.getCreatedAtFrom(), param.getCreatedAtFrom(),
                param.getCreatedAtTo(), param.getCreatedAtTo()
        );
        return total.isEmpty() ? 0 : total.get(0);
    }

    @Override
    public List<User> selectUsersByPage(UserQueryParam param, boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR username LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR is_admin = ?) " +
                "    AND (? IS NULL OR is_blocked = ?) " +
                "    AND (? IS NULL OR created_at >= ?) " +
                "    AND (? IS NULL OR created_at <= ?) " +
                "ORDER BY created_at DESC " +
                "LIMIT ? OFFSET ?";

        return JdbcUtils.executeQuery(
                JdbcUtils.getConnection(),
                sql,
                isAutoCloseConn,
                rs -> {
                    User user = new User();
                    try {
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setPasswordHash(rs.getString("password_hash"));
                        user.setIsAdmin(rs.getInt("is_admin"));
                        user.setIsBlocked(rs.getInt("is_blocked"));
                        user.setBalance(rs.getBigDecimal("balance"));
                        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return user;
                },
                param.getUsername(), param.getUsername(),
                param.getIsAdmin(), param.getIsAdmin(),
                param.getIsBlocked(), param.getIsBlocked(),
                param.getCreatedAtFrom(), param.getCreatedAtFrom(),
                param.getCreatedAtTo(), param.getCreatedAtTo(),
                param.getPageSize(), (param.getPage() - 1) * param.getPageSize()
        );
    }

    @Override
    public void deleteUsersByIds(List<Integer> ids, boolean isAutoCloseConn) throws SQLException {
        String sql = "DELETE FROM users WHERE id IN (" +
                String.join(",", ids.stream().map(id -> "?").toArray(String[]::new)) + ")";
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql, isAutoCloseConn, ids.toArray());
    }

    @Override
    public void addUser(User user, boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, is_admin, is_blocked, balance) " +
                "VALUES (?, ?, ?, ?, ?)";
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql, isAutoCloseConn,
                user.getUsername(),
                user.getPasswordHash(),
                user.getIsAdmin(),
                user.getIsBlocked(),
                user.getBalance()
        );
    }

    @Override
    public void updateUserPassword(User user, boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE users SET password_hash = COALESCE(?, password_hash), " +
                "   updated_at = NOW()" +
                "  WHERE id = ?";
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql, isAutoCloseConn,
                user.getPasswordHash(),
                user.getId()
        );

    }

    @Override
    public void updateUserBalance(User user, boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE users SET balance = COALESCE(?, balance), " +
                "  updated_at = NOW() " +
                " WHERE id = ?";
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql, isAutoCloseConn,
                user.getBalance(),
                user.getId()
        );
    }

    @Override
    public void updateUserStatus(List<Integer> ids, Integer status, boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE users SET is_blocked = ? , " +
                " updated_at = NOW() " +
                " WHERE id IN (" +
                String.join(",", ids.stream().map(id -> "?").toArray(String[]::new)) + ") ";
        List<Object> list = new ArrayList<>();
        list.add(status);
        list.addAll(ids);
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql, isAutoCloseConn,
                list.toArray()
        );
    }
}
