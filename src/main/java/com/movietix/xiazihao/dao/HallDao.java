package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;

import java.sql.SQLException;
import java.util.List;

public interface HallDao {
    //统计影厅数量
    Integer countHalls(HallQueryParam param,boolean isAutoCloseConn) throws SQLException;

    //分页条件查询影厅
    List<Hall> selectHallsByPage(HallQueryParam param,boolean isAutoCloseConn) throws SQLException;

    //添加影厅
    void addHall(Hall hall,boolean isAutoCloseConn) throws SQLException;

    //批量删除影厅
    void deleteHallByIds(List<Integer> ids,boolean isAutoCloseConn);

    //更新影厅信息
    void updateHall(Hall hall,boolean isAutoCloseConn) throws SQLException;

    //根据ID查询影厅
    Hall selectHallById(Integer id,boolean isAutoCloseConn) throws SQLException;
}
