package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.HallDao;
import com.movietix.xiazihao.dao.Impl.HallDaoImpl;
import com.movietix.xiazihao.dao.Impl.MovieDaoImpl;
import com.movietix.xiazihao.dao.Impl.ScreeningDaoImpl;
import com.movietix.xiazihao.dao.MovieDao;
import com.movietix.xiazihao.dao.ScreeningDao;
import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.entity.pojo.Screening;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.ScreeningService;

import java.sql.SQLException;
import java.util.List;

public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningDao screeningDao = new ScreeningDaoImpl();
    private final MovieDao movieDao = new MovieDaoImpl();
    private final HallDao hallDoo = new HallDaoImpl();

    @Override
    public void updateScreening(Screening screening) throws SQLException {

        //获取原来场次的信息
        Screening oldScreening = screeningDao.selectScreeningById(screening.getId(),true);
        //填补电影和放映厅空值
        {
            if (screening.getMovieId() == null) {
                screening.setMovieId(oldScreening.getMovieId());
            }
            if (screening.getHallId() == null) {
                screening.setHallId(oldScreening.getHallId());
            }
        }
        setScreeningEndTimeAndSeats(screening);
        //修改场次
        screeningDao.updateScreening(screening,true);
    }

    @Override
    public Screening selectScreeningById(Integer id) throws SQLException {
        return screeningDao.selectScreeningById(id,true);
    }

    @Override
    public void setScreeningStatus(List<Integer> ids, Integer status) throws SQLException {
        screeningDao.setScreeningStatus(ids, status,true);
    }

    @Override
    public void addScreening(Screening screening) throws SQLException {
        setScreeningEndTimeAndSeats(screening);
        //添加场次
        screeningDao.addScreening(screening,true);
    }

    @Override
    public void deleteScreeningByIds(List<Integer> ids) throws SQLException {
        screeningDao.deleteScreeningByIds(ids,true);
    }

    @Override
    public PageResult<Screening> selectScreeningByPage(ScreeningQueryParam param) throws SQLException {
        Integer total = screeningDao.selectScreeningCount(param,true);
        List<Screening> screeningList = screeningDao.selectScreeningByPage(param,true);
        //根据id找到电影名称
        for (Screening screening : screeningList) {
            Movie movie = movieDao.selectMovieById(screening.getMovieId(), true);
            if (movie != null) {
                screening.setMovieTitle(movie.getTitle());
            }
        }
        //根据id找到影厅名称
        for(Screening screening : screeningList) {
            Hall hall = hallDoo.selectHallById(screening.getHallId(), true);
            if (hall != null) {
                screening.setHallName(hall.getName());
            }
        }
        return new PageResult<>(total, screeningList);
    }

    //设置场次的结束时间和剩余座位数
    private void setScreeningEndTimeAndSeats(Screening screening) throws SQLException {
        //根据id查询电影和影厅的信息
        Movie movie = movieDao.selectMovieById(screening.getMovieId(), true);
        Hall hall = hallDoo.selectHallById(screening.getHallId(), true);
        if (movie == null || hall == null) {
            throw new SQLException("电影或影厅不存在");
        }
        //设置反映场次的结束时间和剩余座位数
        screening.setEndTime(screening.getStartTime().plusMinutes(movie.getDuration()));
        screening.setRemainingSeats(hall.getCapacity());
    }
}
