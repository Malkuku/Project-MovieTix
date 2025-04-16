package com.movietix.xiazihao.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundQueryParam {
    private Integer page = 1;       // 页码(默认1)
    private Integer pageSize = 10; // 每页条数(默认10)
    private Integer orderId;       // 按订单ID筛选
    private Integer userId;       // 按用户ID筛选
    private Integer status;       // 按状态筛选(0-待处理,1-已同意,2-已拒绝)
}
