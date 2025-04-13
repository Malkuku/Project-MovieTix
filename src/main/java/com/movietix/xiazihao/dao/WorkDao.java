package com.movietix.xiazihao.dao;

import com.movietix.xiazihao.entity.param.WorkOrderQueryParam;
import com.movietix.xiazihao.entity.result.WorkOrderResult;

import java.util.List;

public interface WorkDao {
    // 查询订单列表
    List<WorkOrderResult> selectWorkOrders(WorkOrderQueryParam workOrderQueryParam, boolean isAutoCloseConn) throws Exception;
}
