package com.movietix.xiazihao.service.impl;

import com.movietix.xiazihao.dao.Impl.OrderDaoImpl;
import com.movietix.xiazihao.dao.Impl.RefundDaoImpl;
import com.movietix.xiazihao.dao.Impl.UserDaoImpl;
import com.movietix.xiazihao.dao.OrderDao;
import com.movietix.xiazihao.dao.RefundDao;
import com.movietix.xiazihao.dao.UserDao;
import com.movietix.xiazihao.entity.param.RefundQueryParam;
import com.movietix.xiazihao.entity.pojo.Order;
import com.movietix.xiazihao.entity.pojo.Refund;
import com.movietix.xiazihao.entity.pojo.User;
import com.movietix.xiazihao.entity.result.PageResult;
import com.movietix.xiazihao.service.RefundService;
import com.movietix.xiazihao.utils.JdbcUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class RefundServiceImpl implements RefundService {
    private static final RefundDao refundDao = new RefundDaoImpl();
    private static final OrderDao orderDao = new OrderDaoImpl();
    private static final UserDao userDao = new UserDaoImpl();
    @Override
    public PageResult<Refund> selectRefundsByPage(RefundQueryParam refundQueryParam) throws SQLException {
        Integer total = refundDao.selectRefundsCount(refundQueryParam, JdbcUtils.getConnection(), true);
        List<Refund> refundList = refundDao.selectRefundsByPage(refundQueryParam,JdbcUtils.getConnection(), true);
        return new PageResult<>(total, refundList);
    }

    @Override
    public void createRefund(Refund refund) throws SQLException {
        refundDao.createRefund(refund, JdbcUtils.getConnection(), true);
    }

    @Override
    public void processRefunds(List<Integer> ids, Integer adminId, Integer status) throws Exception {
        List<Refund> refundList = refundDao.selectUnprocessRefundsByIds(ids, JdbcUtils.getConnection(), true);
        for(Refund refund : refundList){
            refund.setStatus(status);
            refund.setAdminId(adminId);
            refund.setProcessedAt(LocalDateTime.now());
            JdbcUtils.executeTransaction((conn) -> {
                //更新退款申请表状态
                try {
                    refundDao.updateRefund(refund, conn, false);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                //如果退款成功，更新订单状态，将资金返还给用户
                if(status == 1) {
                    try {
                        Order order = orderDao.selectOrderById(refund.getOrderId(), true);
                        order.setStatus(4);
                        orderDao.updateOrder(order, conn, false);
                        User user = userDao.selectUserById(order.getUserId(), true);
                        user.setBalance(user.getBalance().add(refund.getRefundAmount()));
                        userDao.updateUserBalance(user, conn, false);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            });
        }

    }

    @Override
    public List<Refund> selectRefundsByUserId(Integer userId) throws SQLException {
        return refundDao.selectRefundsByUserId(userId, JdbcUtils.getConnection(), true);
    }
}
