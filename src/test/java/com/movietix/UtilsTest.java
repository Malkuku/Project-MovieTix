package com.movietix;

import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.utils.JdbcUtils;
import com.movietix.xiazihao.utils.JsonUtils;
import com.movietix.xiazihao.utils.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.movietix.xiazihao.utils.JdbcUtils.*;

@Slf4j
public class UtilsTest {

    //测试订单号生成
    @Test
    public void testOrderNoUtils() {
        String orderNo = OrderNoUtils.generateOrderNo();
        log.info("生成的订单号: {}", orderNo);
        String orderNo2 = OrderNoUtils.generateShortOrderNo();
        log.info("生成的短订单号: {}", orderNo2);
        String orderNo3 = OrderNoUtils.generateOrderNoByUserId(1023);
        log.info("生成的订单号: {}", orderNo3);
    }

    //测试Json封装
    @Test
    public void testToJson() {
        Movie movie = new Movie(1, "Example Title", "http://example.com/poster.jpg", LocalDate.parse("2023-10-01"), 120, "Action", BigDecimal.valueOf(8.5), 1, LocalDateTime.now(), LocalDateTime.now());
        String json = JsonUtils.toJson(movie);
        log.info(json);
    }

    //测试Json解析
    @Test
    public void testParseJson() {
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"Example Title\",\n" +
                "  \"poster_url\": \"http://example.com/poster.jpg\",\n" +
                "  \"release_date\": \"2023-10-01\",\n" +
                "  \"duration\": 120,\n" +
                "  \"genre\": \"Action\",\n" +
                "  \"rating\": 8.5,\n" +
                "  \"status\": 1,\n" +
                "  \"created_at\": \"2023-10-01T12:00:00\",\n" +
                "  \"updated_at\": \"2023-10-01T12:00:00\"\n" +
                "}";
        Movie movie = JsonUtils.parseJson(json, Movie.class);
        log.info(movie.toString());
            json = "[\n" +
                    "    {\n" +
                    "        \"id\": 1,\n" +
                    "        \"title\": \"Example Title 1\",\n" +
                    "        \"poster_url\": \"http://example.com/poster1.jpg\",\n" +
                    "        \"release_date\": \"2023-10-01\",\n" +
                    "        \"duration\": 120,\n" +
                    "        \"genre\": \"Action\",\n" +
                    "        \"rating\": 8.5,\n" +
                    "        \"status\": 1,\n" +
                    "        \"created_at\": \"2023-10-01T12:00:00\",\n" +
                    "        \"updated_at\": \"2023-10-01T12:00:00\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 2,\n" +
                    "        \"title\": \"Example Title 2\",\n" +
                    "        \"poster_url\": \"http://example.com/poster2.jpg\",\n" +
                    "        \"release_date\": \"2023-10-02\",\n" +
                    "        \"duration\": 130,\n" +
                    "        \"genre\": \"Comedy\",\n" +
                    "        \"rating\": 7.5,\n" +
                    "        \"status\": 1,\n" +
                    "        \"created_at\": \"2023-10-02T12:00:00\",\n" +
                    "        \"updated_at\": \"2023-10-02T12:00:00\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 3,\n" +
                    "        \"title\": \"Example Title 3\",\n" +
                    "        \"poster_url\": \"http://example.com/poster3.jpg\",\n" +
                    "        \"release_date\": \"2023-10-03\",\n" +
                    "        \"duration\": 140,\n" +
                    "        \"genre\": \"Drama\",\n" +
                    "        \"rating\": 9.0,\n" +
                    "        \"status\": 1,\n" +
                    "        \"created_at\": \"2023-10-03T12:00:00\",\n" +
                    "        \"updated_at\": \"2023-10-03T12:00:00\"\n" +
                    "    }\n" +
                    "]";
        List<Movie> movies = JsonUtils.parseJsonToList(json, Movie.class);
        log.info(movies.toString());
    }

    //测试事务处理
    @Test
    public void testExecuteTransaction() throws Exception {
        List<Integer> result = JdbcUtils.executeTransaction(conn -> {
            String sql2 = "update movies set title = '鹤翼' where id = ?";
            try {
                JdbcUtils.executeUpdate(conn,sql2,true,2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String sql = "select id from movies where title like ?";
            List<Integer> id = null;
            try {
                id = JdbcUtils.executeQuery(conn,sql,true, resultSet -> {
                    Movie movie = new Movie();
                    try {
                        movie.setId(resultSet.getInt("id"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return movie.getId();
                },"鹤翼王");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return id;
        });
        log.info(result.toString());
    }

    //测试查询
    @Test
    public void testExecteQuery() throws SQLException {
        String sql = "select id,title,rating,genre,duration,status,created_at from movies";
        List<Movie> movieList = executeQuery(getConnection(),sql, true,resultSet -> {
            Movie movie = new Movie();
            try {
                //设置日期格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                //从结果集获取内容
                movie.setId(resultSet.getInt("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setRating(resultSet.getBigDecimal("rating"));
                movie.setGenre(resultSet.getString("genre"));
                movie.setDuration(resultSet.getInt("duration"));
                movie.setStatus(resultSet.getInt("status"));
                movie.setCreatedAt(LocalDateTime.parse(resultSet.getString("created_at"), formatter));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return movie;
        });
        log.info(movieList.toString());
    }

    //测试删除
    @Test
    public void testExecteUpdate() throws SQLException {
        String sql = "delete from movies where id = ?";
        executeUpdate(getConnection(),sql,true,4);
    }
}
