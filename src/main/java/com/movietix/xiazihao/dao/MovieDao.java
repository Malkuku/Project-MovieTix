package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieDao {

    //统计电影数量
    Integer countMovies(MovieQueryParam param) throws SQLException;

    //分页条件查询
    List<Movie> selectMoviesByPage(MovieQueryParam param) throws SQLException;
}
