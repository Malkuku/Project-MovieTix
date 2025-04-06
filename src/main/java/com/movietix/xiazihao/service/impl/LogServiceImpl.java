package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.LogDaoImpl;
import com.movietix.xiazihao.dao.LogDao;
import com.movietix.xiazihao.entity.Log;
import com.movietix.xiazihao.service.LogService;

import java.sql.SQLException;
import java.util.List;

public class LogServiceImpl implements LogService {
    private LogDao logDao = new LogDaoImpl();

    @Override
    public List<Log> selectAllLogs() throws SQLException {
        return logDao.selectAllLogs();
    }
}
