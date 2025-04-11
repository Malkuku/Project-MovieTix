package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.PaymentDaoImpl;
import com.movietix.xiazihao.dao.PaymentDao;
import com.movietix.xiazihao.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentDao paymentDao = new PaymentDaoImpl();
}
