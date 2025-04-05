package com.movietix;

import com.movietix.xiazihao.entity.Movie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.movietix.xiazihao.utils.JdbcUtils.executeQuery;

@Slf4j
public class UtilsTest {
    //测试查询
    @Test
    public void testExecteQuery() throws SQLException {
        String sql = "select id,title,rating,genre,duration,status,created_at from movies";
        List<Movie> movieList = executeQuery(sql, resultSet -> {
            Movie movie = new Movie();
            try {
                //设置日期格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                //从结果集获取内容
                movie.setId(resultSet.getInt("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setRating(resultSet.getDouble("rating"));
                movie.setGenre(resultSet.getString("genre"));
                movie.setDuration(resultSet.getInt("duration"));
                movie.setStatus(resultSet.getInt("status"));
                movie.setCreated_at(LocalDateTime.parse(resultSet.getString("created_at"), formatter));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return movie;
        });
        log.info(movieList.toString());
    }
}
