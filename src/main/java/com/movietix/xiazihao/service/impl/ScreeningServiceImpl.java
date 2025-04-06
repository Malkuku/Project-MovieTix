package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.ScreeningDaoImpl;
import com.movietix.xiazihao.dao.ScreeningDao;
import com.movietix.xiazihao.service.ScreeningService;

public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningDao screeningDao = new ScreeningDaoImpl();
}
