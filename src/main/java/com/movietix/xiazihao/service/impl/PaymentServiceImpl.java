package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.PaymentDaoImpl;
import com.movietix.xiazihao.dao.PaymentDao;
import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.PaymentService;

import java.sql.SQLException;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentDao paymentDao = new PaymentDaoImpl();

    @Override
    public PageResult<Payment> selectPaymentsByPage(PaymentQueryParam param) throws SQLException {
        Integer total = paymentDao.selectPaymentsCount(param,true);
        List<Payment> paymentList = paymentDao.selectPaymentsByPage(param,true);
        return new PageResult<>(
                total,
                paymentList
        );
    }
}
