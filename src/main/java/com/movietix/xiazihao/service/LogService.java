package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.Log;

import java.sql.SQLException;
import java.util.List;

public interface LogService {
    //查询所有日志
    List<Log> selectAllLogs() throws SQLException;

    //批量删除日志
    void deleteLogByIds(String[] id_strs) throws SQLException;

    //TODO自动生成日志信息并添加
}
