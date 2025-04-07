package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Screening;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;
import java.util.List;

public interface ScreeningService {

    // 分页查询电影场次
    PageResult<Screening> selectScreeningByPage(ScreeningQueryParam param) throws SQLException;

    // 批量删除电影场次
    void deleteScreeningByIds(List<Integer> ids) throws SQLException;

    //添加电影场次
    void addScreening(Screening screening) throws SQLException;

    //修改电影场次
    void updateScreening(Screening screening) throws SQLException;

    //根据id查询电影场次
    Screening selectScreeningById(Integer id) throws SQLException;

    //批量设置场次状态
    void setScreeningStatus(List<Integer> ids, Integer status) throws SQLException;
}
