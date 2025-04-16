package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.WorkOrderQueryParam;
import com.movietix.xiazihao.entity.pojo.OrderSeat;
import com.movietix.xiazihao.entity.result.WorkOrderResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface WorkDao {
    // 查询订单列表
    List<WorkOrderResult> selectWorkOrders(WorkOrderQueryParam workOrderQueryParam,Connection connection, boolean isAutoCloseConn) throws Exception;

    //根据场次id查询座位
    List<OrderSeat> selectSeatsByScreeningId(Integer screeningId, Connection connection, boolean isAutoCloseConn) throws SQLException;
}
