package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.MovieDaoImpl;
import com.movietix.xiazihao.dao.MovieDao;
import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.MovieService;

import java.sql.SQLException;
import java.util.List;

public class MovieServiceImpl implements MovieService {
    private final MovieDao movieDao = new MovieDaoImpl();

    @Override
    public PageResult<Movie> selectMoviesByPage(MovieQueryParam param) throws SQLException {
        Integer total = movieDao.countMovies(param);
        List<Movie> movies = movieDao.selectMoviesByPage(param);

        return new PageResult<>(total, movies);
    }
}
