package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.RefundQueryParam;
import com.movietix.xiazihao.entity.pojo.Refund;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;

public interface RefundService {
    // 条件分页查询退票记录
    PageResult<Refund> selectRefundsByPage(RefundQueryParam refundQueryParam) throws SQLException;
}
