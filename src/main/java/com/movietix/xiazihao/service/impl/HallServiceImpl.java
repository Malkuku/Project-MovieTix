package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.HallDao;
import com.movietix.xiazihao.dao.Impl.HallDaoImpl;
import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.HallService;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class HallServiceImpl implements HallService {

    private final HallDao hallDao = new HallDaoImpl();

    @Override
    public Hall selectHallById(Integer id) {
        try {
            return hallDao.selectHallById(id,JdbcUtils.getConnection(),true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateHall(Hall hall) {
        try {
            hallDao.updateHall(hall,JdbcUtils.getConnection(),true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteHallsByIds(List<Integer> ids) throws SQLException {
        hallDao.deleteHallByIds(ids,JdbcUtils.getConnection(),true);
    }

    @Override
    public void addHall(Hall hall) {
        try {
            hallDao.addHall(hall,JdbcUtils.getConnection(),true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<Hall> selectHallsByPage(HallQueryParam param) {
        try {
            Integer total = hallDao.countHalls(param, JdbcUtils.getConnection(),true);
            List<Hall> hallList = hallDao.selectHallsByPage(param,JdbcUtils.getConnection(),true);
            return new PageResult<>(total, hallList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
