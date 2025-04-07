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
    public Movie selectMovieById(Integer id) {
        try {
            return movieDao.selectMovieById(id,true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMovie(Movie movie) {
        try {
            movieDao.updateMovie(movie,true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addMovie(Movie movie) {
        try {
            movieDao.addMovie(movie,true);
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
           movieDao.deleteMoviesByIds(ids,true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<Movie> selectMoviesByPage(MovieQueryParam param) throws SQLException {
        Integer total = movieDao.countMovies(param,true);
        List<Movie> movies = movieDao.selectMoviesByPage(param,true);

        return new PageResult<>(total, movies);
    }
}
