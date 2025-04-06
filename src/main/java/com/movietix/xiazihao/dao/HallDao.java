package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;

import java.sql.SQLException;
import java.util.List;

public interface HallDao {
    //统计影厅数量
    Integer countHalls(HallQueryParam param) throws SQLException;

    //分页条件查询影厅
    List<Hall> selectHallsByPage(HallQueryParam param) throws SQLException;

    //添加影厅
    void addHall(Hall hall) throws SQLException;
}
