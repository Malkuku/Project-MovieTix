package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.HallDao;
import com.movietix.xiazihao.dao.Impl.HallDaoImpl;
import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.HallService;

import java.sql.SQLException;
import java.util.List;

public class HallServiceImpl implements HallService {

    private final HallDao hallDao = new HallDaoImpl();

    @Override
    public PageResult<Hall> selectHallsByPage(HallQueryParam param) {
        try {
            Integer total = hallDao.countHalls(param);
            List<Hall> hallList = hallDao.selectHallsByPage(param);
            return new PageResult<>(total, hallList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
