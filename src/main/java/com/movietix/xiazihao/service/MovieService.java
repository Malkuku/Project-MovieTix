package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;

public interface MovieService {
    //分页条件查询电影
    PageResult<Movie> selectMoviesByPage(MovieQueryParam param) throws SQLException;
}
