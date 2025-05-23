package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.PaymentQueryParam;
import com.movietix.xiazihao.entity.pojo.Payment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface PaymentDao {
    // 查询支付记录总数
    Integer selectPaymentsCount(PaymentQueryParam param,Connection conn, boolean isAutoCloseConn) throws SQLException;
    // 条件分页查询支付记录
    List<Payment> selectPaymentsByPage(PaymentQueryParam param,Connection conn,boolean isAutoCloseConn) throws SQLException;

    //添加支付记录
    void addPayment(Payment payment,Connection conn,boolean isAutoCloseConn) throws SQLException;

    // 更新支付记录
    void updatePayment(Payment payment, Connection conn, boolean isAutoCloseConn) throws SQLException;

    //通过ID查询支付记录
    Payment selectPaymentById(Integer id,Connection conn, boolean isAutoCloseConn) throws SQLException;

    //获取支付记录最大ID
    Integer selectPaymentsMaxId(Connection conn,boolean isAutoCloseConn) throws SQLException;
}
