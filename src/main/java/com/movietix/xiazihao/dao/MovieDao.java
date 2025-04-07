package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieDao {

    //统计电影数量
    Integer countMovies(MovieQueryParam param,boolean isAutoCloseConn) throws SQLException;

    //分页条件查询
    List<Movie> selectMoviesByPage(MovieQueryParam param,boolean isAutoCloseConn) throws SQLException;

    //批量删除电影
    void deleteMoviesByIds(List<Integer> ids,boolean isAutoCloseConn) throws SQLException;

    //添加电影
    void addMovie(Movie movie,boolean isAutoCloseConn) throws SQLException;

    //根据ID修改电影
    void updateMovie(Movie movie,boolean isAutoCloseConn) throws SQLException;

    //根据ID查询电影
    Movie selectMovieById(Integer id,boolean isAutoCloseConn) throws SQLException;
}
