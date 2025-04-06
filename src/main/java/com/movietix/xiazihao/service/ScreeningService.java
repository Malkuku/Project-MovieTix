package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.ScreeningQueryParam;
import com.movietix.xiazihao.entity.pojo.Movie;
import com.movietix.xiazihao.entity.pojo.Screening;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;
import java.util.List;

public interface ScreeningService {

    // 分页查询电影场次
    PageResult<Screening> selectScreeningByPage(ScreeningQueryParam param) throws SQLException;

    // 批量删除电影场次
    void deleteScreeningByIds(List<Integer> ids) throws SQLException;
}
