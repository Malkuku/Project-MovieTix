package com.movietix.xiazihao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Refund {
    private Integer id; // 主键ID
    private Integer orderId; // 订单ID
    private Integer userId; // 用户ID
    private String reason; // 退票原因
    private Integer status = 0; // 状态:0-待处理 1-已同意 2-已拒绝
    private Integer adminId; // 处理管理员ID
    private LocalDateTime processedAt; // 处理时间
    private BigDecimal refundAmount; // 退款金额
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
