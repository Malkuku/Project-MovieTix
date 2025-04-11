package com.movietix.xiazihao.service;

import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;
import com.movietix.xiazihao.entity.result.PageResult;

import java.sql.SQLException;

public interface PaymentService {
    // 条件分页查询支付记录
    PageResult<Payment> selectPaymentsByPage(PaymentQueryParam param) throws SQLException;

    //添加支付记录
    void addPayment(Payment payment) throws SQLException;

    // 更新支付记录
    void updatePayment(Payment payment) throws SQLException;

    // 根据id查询支付记录
    Payment selectPaymentById(Integer id) throws SQLException;
}
