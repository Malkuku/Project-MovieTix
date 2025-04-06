package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.ScreeningDaoImpl;
import com.movietix.xiazihao.dao.ScreeningDao;
import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Screening;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.ScreeningService;

import java.sql.SQLException;
import java.util.List;

public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningDao screeningDao = new ScreeningDaoImpl();

    @Override
    public PageResult<Screening> selectScreeningByPage(ScreeningQueryParam param) throws SQLException {
        Integer total = screeningDao.selectScreeningCount(param);
        List<Screening> screeningList = screeningDao.selectScreeningByPage(param);
        return new PageResult<>(total, screeningList);
    }
}
