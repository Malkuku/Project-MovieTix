package com.movietix.xiazihao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer id; // 主键ID
    private String orderNo; // 订单编号
    private Integer userId; // 用户ID
    private Integer screeningId; // 场次ID
    private BigDecimal totalAmount; // 总金额
    private Integer seatCount; // 座位数
    private Integer status = 0; // 状态:0-待支付 1-已支付 2-已取消 3-已完成 4-已退款
    private String contactPhone; // 联系电话
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
