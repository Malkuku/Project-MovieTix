package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDao {
    // 查询支付记录总数
    Integer selectPaymentsCount(PaymentQueryParam param,boolean isAutoCloseConn) throws SQLException;
    // 条件分页查询支付记录
    List<Payment> selectPaymentsByPage(PaymentQueryParam param,boolean isAutoCloseConn) throws SQLException;

    //添加支付记录
    void addPayment(Payment payment,boolean isAutoCloseConn) throws SQLException;

    // 更新支付记录
    void updatePayment(Payment payment, boolean isAutoCloseConn) throws SQLException;

    Payment selectPaymentById(Integer id, boolean isAutoCloseConn) throws SQLException;
}
