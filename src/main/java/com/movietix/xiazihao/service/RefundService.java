package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.RefundQueryParam;
import com.movietix.xiazihao.entity.pojo.Refund;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;
import java.util.List;

public interface RefundService {
    // 条件分页查询退票记录
    PageResult<Refund> selectRefundsByPage(RefundQueryParam refundQueryParam) throws SQLException;

    // 创建退票记录
    void createRefund(Refund refund) throws SQLException;

    // 批量处理退票记录
    void processRefunds(List<Integer> ids, Integer adminId, Integer status) throws Exception;

    // 根据用户ID查询退票记录
    List<Refund> selectRefundsByUserId(Integer userId) throws SQLException;
}
