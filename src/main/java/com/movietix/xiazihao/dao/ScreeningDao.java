package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Screening;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ScreeningDao {
    // 查询电影场次总数
    Integer selectScreeningCount(ScreeningQueryParam param, Connection conn, boolean isAutoCloseConn) throws SQLException;
    // 查询电影场次列表
    List<Screening> selectScreeningByPage(ScreeningQueryParam param, Connection conn,boolean isAutoCloseConn) throws SQLException;

    // 批量删除电影场次
    void deleteScreeningByIds(List<Integer> ids, Connection conn,boolean isAutoCloseConn) throws SQLException;

    // 添加电影场次
    void addScreening(Screening screening, Connection conn, boolean isAutoCloseConn) throws SQLException;

    //修改电影场次
    void updateScreening(Screening screening, Connection conn, boolean isAutoCloseConn) throws SQLException;

    //根据id查询电影场次
    Screening selectScreeningById(Integer id, Connection conn,boolean isAutoCloseConn) throws SQLException;

    //批量设置场次状态
    void setScreeningStatus(List<Integer> ids, Integer status, Connection conn, boolean isAutoCloseConn) throws SQLException;
}
