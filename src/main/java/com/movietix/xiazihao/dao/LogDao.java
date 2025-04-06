package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.Log;

import java.sql.SQLException;
import java.util.List;

public interface LogDao {
    //查询所有日志
    List<Log> selectAllLogs() throws SQLException;

    //批量删除日志
    void deleteLogByIds(List<Integer> ids) throws SQLException;
}
