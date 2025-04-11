package com.movietix.xiazihao.entity.param;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderQueryParam {
    private String orderNo;              // 订单编号(精确查询)
    private Integer userId;              // 用户ID
    private Integer screeningId;        // 场次ID
    private Integer status;              // 订单状态(0-待支付 1-已支付 2-已取消 3-已完成 4-已退款)
    private BigDecimal minAmount;        // 最小金额
    private BigDecimal maxAmount;        // 最大金额
    private LocalDateTime startDate;            // 创建开始日期(YYYY-MM-DD HH:mm:ss)
    private LocalDateTime endDate;             // 创建结束日期
    private Integer page = 1;           // 页码(默认1)
    private Integer pageSize = 10;      // 每页条数(默认10)
}
