package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.HallDao;
import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class HallDaoImpl implements HallDao {

    @Override
    public Hall selectHallById(Integer id,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * FROM halls WHERE id = ?";
        List<Hall> hallList = JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Hall hall = new Hall();
                    try {
                        hall.setId(rs.getInt("id"));
                        hall.setName(rs.getString("name"));
                        hall.setCapacity(rs.getInt("capacity"));
                        hall.setRows(rs.getInt("rows"));
                        hall.setCols(rs.getInt("cols"));
                        hall.setFacilities(rs.getString("facilities"));
                        hall.setStatus(rs.getInt("status"));
                        hall.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        hall.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return hall;
                },
                id
        );
        return hallList.isEmpty() ? null : hallList.get(0);
    }

    @Override
    public void updateHall(Hall hall,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE halls SET " +
                "name = COALESCE(?, name), " +
                "capacity = COALESCE(?, capacity), " +
                "`rows` = COALESCE(?, `rows`), " +
                "`cols` = COALESCE(?, `cols`), " +
                "facilities = COALESCE(?, facilities), " +
                "status = COALESCE(?, status), " +
                "updated_at = NOW() " +
                "WHERE id = ?";
        JdbcUtils.executeUpdate(
                conn,
                sql,
                isAutoCloseConn,
                hall.getName(),
                hall.getCapacity(),
                hall.getRows(),
                hall.getCols(),
                hall.getFacilities(),
                hall.getStatus(),
                hall.getId()
        );
    }

    @Override
    public void deleteHallByIds(List<Integer> ids,Connection conn,boolean isAutoCloseConn) {
        String sql = "DELETE FROM halls WHERE id IN (" + String.join(",",ids.stream().map(id -> "?").toArray(String[]::new)) + ")";
        try {
            JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn, ids.toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addHall(Hall hall,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO halls (name, capacity, `rows`, `cols`, facilities, status) VALUES (?, ?, ?, ?, ?, ?)";
        JdbcUtils.executeUpdate(
                conn,
                sql,
                isAutoCloseConn,
                hall.getName(),
                hall.getCapacity(),
                hall.getRows(),
                hall.getCols(),
                hall.getFacilities(),
                hall.getStatus()
        );
    }

    @Override
    public List<Hall> selectHallsByPage(HallQueryParam param,Connection conn,boolean isAutoCloseConn) throws SQLException, SQLException {
        String sql = """
                SELECT *
                FROM halls
                WHERE 1=1\s
                    AND (? IS NULL OR name LIKE CONCAT('%', ?, '%'))
                    AND (? IS NULL OR capacity >= ?)
                    AND (? IS NULL OR capacity <= ?)
                    AND (? IS NULL OR `rows` >= ?)
                    AND (? IS NULL OR `cols` >= ?)
                    AND (? IS NULL OR facilities LIKE CONCAT('%', ?, '%'))
                    AND (? IS NULL OR status = ?)
                ORDER BY updated_at desc
                LIMIT ? OFFSET ?""";

        return JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Hall hall = new Hall();
                    try {
                        hall.setId(rs.getInt("id"));
                        hall.setName(rs.getString("name"));
                        hall.setCapacity(rs.getInt("capacity"));
                        hall.setRows(rs.getInt("rows"));
                        hall.setCols(rs.getInt("cols"));
                        hall.setFacilities(rs.getString("facilities"));
                        hall.setStatus(rs.getInt("status"));
                        hall.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        hall.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return hall;
                },
                param.getName(), param.getName(),
                param.getMinCapacity(), param.getMinCapacity(),
                param.getMaxCapacity(), param.getMaxCapacity(),
                param.getMinRows(), param.getMinRows(),
                param.getMinCols(), param.getMinCols(),
                param.getFacility(), param.getFacility(),
                param.getStatus(), param.getStatus(),
                param.getPageSize(), (param.getPage() - 1) * param.getPageSize()
        );
    }

    @Override
    public Integer countHalls(HallQueryParam param, Connection conn,boolean isAutoCloseConn) throws SQLException{
        String sql = "SELECT COUNT(*)\n" +
                "FROM halls\n" +
                "WHERE 1=1 \n" +
                "    AND (? IS NULL OR name LIKE CONCAT('%', ?, '%'))\n" +
                "    AND (? IS NULL OR capacity >= ?)\n" +
                "    AND (? IS NULL OR capacity <= ?)\n" +
                "    AND (? IS NULL OR `rows` >= ?)\n" +
                "    AND (? IS NULL OR `cols` >= ?)\n" +
                "    AND (? IS NULL OR facilities LIKE CONCAT('%', ?, '%'))\n" +
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
                param.getName(), param.getName(),
                param.getMinCapacity(), param.getMinCapacity(),
                param.getMaxCapacity(), param.getMaxCapacity(),
                param.getMinRows(), param.getMinRows(),
                param.getMinCols(), param.getMinCols(),
                param.getFacility(), param.getFacility(),
                param.getStatus(), param.getStatus()
        );
        return total.get(0);
    }
}
