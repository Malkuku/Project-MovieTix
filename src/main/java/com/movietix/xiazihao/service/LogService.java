package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.Log;

import java.sql.SQLException;
import java.util.List;

public interface LogService {
    List<Log> selectAllLogs() throws SQLException;

    //TODO自动生成日志信息并添加
}
