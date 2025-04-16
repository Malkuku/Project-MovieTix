package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.RefundDaoImpl;
import com.movietix.xiazihao.dao.RefundDao;
import com.movietix.xiazihao.service.RefundService;

public class RefundServiceImpl implements RefundService {
    private static final RefundDao refundDao = new RefundDaoImpl();
}
