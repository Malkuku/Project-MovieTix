package com.movietix;

import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.utils.JdbcUtils;
import com.movietix.xiazihao.utils.JsonUtils;
import com.movietix.xiazihao.utils.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
        String orderNo = OrderNoUtils.generateOrderNo(12234);
        log.info("生成的订单号: {}", orderNo);
    }

    //测试Json封装
    @Test
    public void testToJson() {
        Movie movie = new Movie(1, "Example Title", "http://example.com/poster.jpg", LocalDate.parse("2023-10-01"), 120, "Action", 8.5, 1, LocalDateTime.now(), LocalDateTime.now());
        String json = JsonUtils.toJson(movie);
        log.info(json);
    }

    //测试Json解析
    @Test
    public void testParseJson() {
        String json = """
                {
                  "id": 1,
                  "title": "Example Title",
                  "poster_url": "http://example.com/poster.jpg",
                  "release_date": "2023-10-01",
                  "duration": 120,
                  "genre": "Action",
                  "rating": 8.5,
                  "status": 1,
                  "created_at": "2023-10-01T12:00:00",
                  "updated_at": "2023-10-01T12:00:00"
                }""";
        Movie movie = JsonUtils.parseJson(json, Movie.class);
        log.info(movie.toString());
            json = """
                    [
                        {
                            "id": 1,
                            "title": "Example Title 1",
                            "poster_url": "http://example.com/poster1.jpg",
                            "release_date": "2023-10-01",
                            "duration": 120,
                            "genre": "Action",
                            "rating": 8.5,
                            "status": 1,
                            "created_at": "2023-10-01T12:00:00",
                            "updated_at": "2023-10-01T12:00:00"
                        },
                        {
                            "id": 2,
                            "title": "Example Title 2",
                            "poster_url": "http://example.com/poster2.jpg",
                            "release_date": "2023-10-02",
                            "duration": 130,
                            "genre": "Comedy",
                            "rating": 7.5,
                            "status": 1,
                            "created_at": "2023-10-02T12:00:00",
                            "updated_at": "2023-10-02T12:00:00"
                        },
                        {
                            "id": 3,
                            "title": "Example Title 3",
                            "poster_url": "http://example.com/poster3.jpg",
                            "release_date": "2023-10-03",
                            "duration": 140,
                            "genre": "Drama",
                            "rating": 9.0,
                            "status": 1,
                            "created_at": "2023-10-03T12:00:00",
                            "updated_at": "2023-10-03T12:00:00"
                        }
                    ]""";
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
            List<Integer> id;
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
                movie.setRating(resultSet.getDouble("rating"));
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
