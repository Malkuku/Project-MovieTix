package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.ScreeningDao;
import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Screening;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScreeningDaoImpl implements ScreeningDao {

    @Override
    public void updateScreening(Screening screening, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE screenings SET " +
                "movie_id = COALESCE(?, movie_id), " +
                "hall_id = COALESCE(?, hall_id), " +
                "start_time = COALESCE(?, start_time), " +
                "end_time = COALESCE(?, end_time), " +
                "price = COALESCE(?, price), " +
                "remaining_seats = COALESCE(?, remaining_seats), " +
                "status = COALESCE(?, status), " +
                "updated_at = NOW() " +
                "WHERE id = ?";
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,
                screening.getMovieId(),
                screening.getHallId(),
                screening.getStartTime(),
                screening.getEndTime(),
                screening.getPrice(),
                screening.getRemainingSeats(),
                screening.getStatus(),
                screening.getId());
    }

    @Override
    public Screening selectScreeningById(Integer id, Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT * FROM screenings WHERE id = ?";
        List<Screening> screeningList = JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Screening screening = new Screening();
                    try {
                        screening.setId(rs.getInt("id"));
                        screening.setMovieId(rs.getInt("movie_id"));
                        screening.setHallId(rs.getInt("hall_id"));
                        screening.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                        screening.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                        screening.setPrice(rs.getDouble("price"));
                        screening.setRemainingSeats(rs.getInt("remaining_seats"));
                        screening.setStatus(rs.getInt("status"));
                        screening.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        screening.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return screening;
                },
                id
        );
        return screeningList.isEmpty() ? null : screeningList.get(0);
    }

    @Override
    public void setScreeningStatus(List<Integer> ids, Integer status, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "UPDATE screenings SET status = ? WHERE id IN (" + String.join(",", ids.stream().map(id -> "?").toArray(String[]::new)) + ")";
        List<Object> params = new ArrayList<>();
        params.add(status);
        params.addAll(ids);
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,
                params.toArray());
    }

    @Override
    public void addScreening(Screening screening, Connection conn, boolean isAutoCloseConn) throws SQLException {
        String sql = "INSERT INTO screenings (movie_id, hall_id, start_time, end_time, price, remaining_seats, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,
                screening.getMovieId(),
                screening.getHallId(),
                screening.getStartTime(),
                screening.getEndTime(),
                screening.getPrice(),
                screening.getRemainingSeats(),
                screening.getStatus());
    }

    @Override
    public void deleteScreeningByIds(List<Integer> ids,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "DELETE FROM screenings WHERE id IN (" + String.join(",", ids.stream().map(id->"?").toArray(String[]::new)) + ")";
        JdbcUtils.executeUpdate(conn, sql, isAutoCloseConn,ids.toArray());
    }

    @Override
    public Integer selectScreeningCount(ScreeningQueryParam param,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM screenings s " +
                "JOIN movies m ON s.movie_id = m.id " +
                "JOIN halls h ON s.hall_id = h.id " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR s.movie_id = ?) " +
                "    AND (? IS NULL OR s.hall_id = ?) " +
                "    AND (? IS NULL OR m.title LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR h.name LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR s.start_time >= ?) " +
                "    AND (? IS NULL OR s.start_time <= ?) " +
                "    AND (? IS NULL OR s.price >= ?) " +
                "    AND (? IS NULL OR s.price <= ?) " +
                "    AND (? IS NULL OR s.remaining_seats >= ?) " +
                "    AND (? IS NULL OR s.status = ?)";

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
                param.getMovieId(), param.getMovieId(),
                param.getHallId(), param.getHallId(),
                param.getMovieTitle(), param.getMovieTitle(),
                param.getHallName(), param.getHallName(),
                param.getStartTimeFrom(), param.getStartTimeFrom(),
                param.getStartTimeTo(), param.getStartTimeTo(),
                param.getMinPrice(), param.getMinPrice(),
                param.getMaxPrice(), param.getMaxPrice(),
                param.getMinSeats(), param.getMinSeats(),
                param.getStatus(), param.getStatus()
        );
        return total.isEmpty() ? 0 : total.get(0);
    }

    @Override
    public List<Screening> selectScreeningByPage(ScreeningQueryParam param,Connection conn,boolean isAutoCloseConn) throws SQLException {
        String sql = "SELECT s.*, m.title AS movie_title, h.name AS hall_name " +
                "FROM screenings s " +
                "JOIN movies m ON s.movie_id = m.id " +
                "JOIN halls h ON s.hall_id = h.id " +
                "WHERE 1=1 " +
                "    AND (? IS NULL OR s.movie_id = ?) " +
                "    AND (? IS NULL OR s.hall_id = ?) " +
                "    AND (? IS NULL OR m.title LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR h.name LIKE CONCAT('%', ?, '%')) " +
                "    AND (? IS NULL OR s.start_time >= ?) " +
                "    AND (? IS NULL OR s.start_time <= ?) " +
                "    AND (? IS NULL OR s.price >= ?) " +
                "    AND (? IS NULL OR s.price <= ?) " +
                "    AND (? IS NULL OR s.remaining_seats >= ?) " +
                "    AND (? IS NULL OR s.status = ?) " +
                "ORDER BY updated_at desc " +
                "LIMIT ? OFFSET ?";

        return JdbcUtils.executeQuery(
                conn,
                sql,
                isAutoCloseConn,
                rs -> {
                    Screening screening = new Screening();
                    try {
                        screening.setId(rs.getInt("id"));
                        screening.setMovieId(rs.getInt("movie_id"));
                        screening.setHallId(rs.getInt("hall_id"));
                        screening.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                        screening.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                        screening.setPrice(rs.getDouble("price"));
                        screening.setRemainingSeats(rs.getInt("remaining_seats"));
                        screening.setStatus(rs.getInt("status"));
                        screening.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        screening.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return screening;
                },
                param.getMovieId(), param.getMovieId(),
                param.getHallId(), param.getHallId(),
                param.getMovieTitle(), param.getMovieTitle(),
                param.getHallName(), param.getHallName(),
                param.getStartTimeFrom(), param.getStartTimeFrom(),
                param.getStartTimeTo(), param.getStartTimeTo(),
                param.getMinPrice(), param.getMinPrice(),
                param.getMaxPrice(), param.getMaxPrice(),
                param.getMinSeats(), param.getMinSeats(),
                param.getStatus(), param.getStatus(),
                param.getPageSize(), (param.getPage() - 1) * param.getPageSize()
        );
    }
}
