package com.movietix.xiazihao.dao.Impl;

import com.movietix.xiazihao.dao.MovieDao;
import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class MovieDaoImpl implements MovieDao {

    @Override
    public void updateMovie(Movie movie) throws SQLException {
        String sql = "UPDATE movies SET " +
                "title = COALESCE(?, title), " +
                "release_date = COALESCE(?, release_date), " +
                "poster_url = COALESCE(?, poster_url), " +
                "duration = COALESCE(?, duration), " +
                "genre = COALESCE(?, genre), " +
                "rating = COALESCE(?, rating), " +
                "status = COALESCE(?, status), " +
                "updated_at = NOW() " +
                "WHERE id = ?";
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql,
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getPosterUrl(),
                movie.getDuration(),
                movie.getGenre(),
                movie.getRating(),
                movie.getStatus(),
                movie.getId()
        );
    }

    @Override
    public void addMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, release_date, poster_url, duration, genre, rating, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        log.info("Executing SQL: {}", sql);
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql,
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getPosterUrl(),
                movie.getDuration(),
                movie.getGenre(),
                movie.getRating(),
                movie.getStatus()
        );
    }

    @Override
    public void deleteMoviesByIds(List<Integer> ids) throws SQLException {
        String sql = "DELETE FROM movies WHERE id IN (" +
                String.join(",", ids.stream().map(id -> "?").toArray(String[]::new)) +
                ")";
        log.info("Executing SQL: {}", sql);
        JdbcUtils.executeUpdate(JdbcUtils.getConnection(), sql, ids.toArray());
    }

    @Override
    public Integer countMovies(MovieQueryParam param) throws SQLException {
        String sql = "SELECT COUNT(*) FROM movies WHERE 1=1 " +
                "AND (? IS NULL OR title LIKE CONCAT('%', ?, '%')) " +
                "AND (? IS NULL OR release_date = ?) " +
                "AND (? IS NULL OR duration >= ?) " +
                "AND (? IS NULL OR duration <= ?) " +
                "AND (? IS NULL OR genre = ?) " +
                "AND (? IS NULL OR rating >= ?) " +
                "AND (? IS NULL OR rating <= ?) " +
                "AND (? IS NULL OR status = ?)";


        List<Integer> total = JdbcUtils.executeQuery(
                JdbcUtils.getConnection(),
                sql,
                rs -> {
                    Integer count = 0;
                    try {
                        count = rs.getInt(1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return count;
                },
                param.getTitle(), param.getTitle(),// title
                param.getReleaseDate(), param.getReleaseDate(), // release_date
                param.getMinDuration(), param.getMinDuration(),  // min_duration
                param.getMaxDuration(), param.getMaxDuration(),  // max_duration
                param.getGenre(), param.getGenre(),              // genre
                param.getMinRating(), param.getMinRating(),     // min_rating
                param.getMaxRating(), param.getMaxRating(),     // max_rating
                param.getStatus(), param.getStatus()            // status
        );
        return total.get(0);
    }

    @Override
    public List<Movie> selectMoviesByPage(MovieQueryParam param) throws SQLException {
        String sql = "SELECT *\n" +
                "FROM movies\n" +
                "WHERE 1=1 \n" +
                "    AND (? IS NULL OR title LIKE CONCAT('%', ?, '%'))\n" +
                "    AND (? IS NULL OR release_date = ?)\n" +
                "    AND (? IS NULL OR duration >= ?)\n" +
                "    AND (? IS NULL OR duration <= ?)\n" +
                "    AND (? IS NULL OR genre = ?)\n" +
                "    AND (? IS NULL OR rating >= ?)\n" +
                "    AND (? IS NULL OR rating <= ?)\n" +
                "    AND (? IS NULL OR status = ?)\n" +
                "ORDER BY id \n" +
                "LIMIT ? OFFSET ?";

        return JdbcUtils.executeQuery(
                JdbcUtils.getConnection(),
                sql,
                rs -> {
                    Movie movie = new Movie();
                    try {
                        movie.setId(rs.getInt("id"));
                        movie.setTitle(rs.getString("title"));
                        movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
                        movie.setPosterUrl(rs.getString("poster_url"));
                        movie.setDuration(rs.getInt("duration"));
                        movie.setGenre(rs.getString("genre"));
                        movie.setRating(rs.getBigDecimal("rating"));
                        movie.setStatus(rs.getInt("status"));
                        movie.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                        movie.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return movie;
                },
                param.getTitle(), param.getTitle(),
                param.getReleaseDate(), param.getReleaseDate(),
                param.getMinDuration(), param.getMinDuration(),
                param.getMaxDuration(), param.getMaxDuration(),
                param.getGenre(), param.getGenre(),
                param.getMinRating(), param.getMinRating(),
                param.getMaxRating(), param.getMaxRating(),
                param.getStatus(), param.getStatus(),
                param.getPageSize(), (param.getPage() - 1) * param.getPageSize()
        );
    }
}
