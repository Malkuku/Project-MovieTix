package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Screening;

import java.sql.SQLException;
import java.util.List;

public interface ScreeningDao {
    // 查询电影场次总数
    Integer selectScreeningCount(ScreeningQueryParam param) throws SQLException;
    // 查询电影场次列表
    List<Screening> selectScreeningByPage(ScreeningQueryParam param) throws SQLException;

    // 批量删除电影场次
    void deleteScreeningByIds(List<Integer> ids) throws SQLException;
}
