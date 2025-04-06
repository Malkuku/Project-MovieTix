package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.Log;

import java.sql.SQLException;
import java.util.List;

public interface LogDao {
    List<Log> selectAllLogs() throws SQLException;

}
