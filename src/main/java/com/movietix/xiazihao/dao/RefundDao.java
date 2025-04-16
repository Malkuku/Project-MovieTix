package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.RefundQueryParam;
import com.movietix.xiazihao.entity.pojo.Refund;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RefundDao {
    // 查询退票记录总数
    Integer selectRefundsCount(RefundQueryParam refundQueryParam, Connection conn, boolean isAutoCloseConn) throws SQLException;
    // 分页查询退票记录
    List<Refund> selectRefundsByPage(RefundQueryParam refundQueryParam, Connection conn, boolean isAutoCloseConn) throws SQLException;
}
