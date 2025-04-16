package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.RefundDaoImpl;
import com.movietix.xiazihao.dao.RefundDao;
import com.movietix.xiazihao.entity.param.RefundQueryParam;
import com.movietix.xiazihao.entity.pojo.Refund;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.RefundService;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class RefundServiceImpl implements RefundService {
    private static final RefundDao refundDao = new RefundDaoImpl();

    @Override
    public PageResult<Refund> selectRefundsByPage(RefundQueryParam refundQueryParam) throws SQLException {
        Integer total = refundDao.selectRefundsCount(refundQueryParam, JdbcUtils.getConnection(), true);
        List<Refund> refundList = refundDao.selectRefundsByPage(refundQueryParam,JdbcUtils.getConnection(), true);
        return new PageResult<>(total, refundList);
    }
}
