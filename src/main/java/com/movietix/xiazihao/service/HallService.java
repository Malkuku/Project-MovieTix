package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.HallQueryParam;
import com.movietix.xiazihao.entity.pojo.Hall;
import com.movietix.xiazihao.entity.result.PageResult;

public interface HallService {
    // 分页条件查询影厅
    PageResult<Hall> selectHallsByPage(HallQueryParam param);

    // 添加影厅
    void addHall(Hall hall);
}
