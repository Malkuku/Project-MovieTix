package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.MovieDaoImpl;
import com.movietix.xiazihao.dao.MovieDao;
import com.movietix.xiazihao.entity.param.MovieQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.MovieService;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class MovieServiceImpl implements MovieService {
    private final MovieDao movieDao = new MovieDaoImpl();

    @Override
    public Movie selectMovieById(Integer id) {
        try {
            return movieDao.selectMovieById(id, JdbcUtils.getConnection(),true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMovie(Movie movie) {
        try {
            movieDao.updateMovie(movie, JdbcUtils.getConnection(),true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addMovie(Movie movie) {
        try {
            movieDao.addMovie(movie, JdbcUtils.getConnection(),true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMoviesByIds(List<Integer> ids){
        try {
           if(ids == null || ids.isEmpty()) {
                return;
           }
           movieDao.deleteMoviesByIds(ids, JdbcUtils.getConnection(),true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<Movie> selectMoviesByPage(MovieQueryParam param) throws SQLException {
        Integer total = movieDao.countMovies(param, JdbcUtils.getConnection(),true);
        List<Movie> movies = movieDao.selectMoviesByPage(param, JdbcUtils.getConnection(),true);
        return new PageResult<>(total, movies);
    }

    @Override
    public Double selectMovieLowestPriceByScreeningId(Integer movieId) throws SQLException {
        return movieDao.selectMovieLowestPriceByScreeningId(movieId, JdbcUtils.getConnection(),true);
    }
}
