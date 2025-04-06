package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.LogDaoImpl;
import com.movietix.xiazihao.dao.LogDao;
import com.movietix.xiazihao.entity.pojo.Log;
import com.movietix.xiazihao.service.LogService;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LogServiceImpl implements LogService {
    private final LogDao logDao = new LogDaoImpl();

    @Override
    public List<Log> selectAllLogs() throws SQLException {
        return logDao.selectAllLogs();
    }

    @Override
    public void deleteLogByIds(String[] id_strs) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        log.info("Received log IDs for deletion: {}", (Object) id_strs);
        if(id_strs != null) {
            for (String id_str : id_strs) {
                log.info("Processing ID: {}", id_str);
                try {
                    ids.add(Integer.parseInt(id_str));
                } catch (NumberFormatException e) {
                    log.error("Invalid ID format: {}", id_str, e);
                }
            }
            log.info("Parsed IDs for deletion: {}", ids);
            logDao.deleteLogByIds(ids);
        }
    }
}
