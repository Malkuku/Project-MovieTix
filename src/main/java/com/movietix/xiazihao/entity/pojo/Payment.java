package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Integer id; // 主键ID
    private Integer orderId; // 订单ID
    private BigDecimal amount; // 支付金额
    private String paymentMethod; // 支付方式
    private String transactionId; // 交易号
    private Integer status = 0; // 状态:0-未支付 1-支付成功 2-支付失败
    private LocalDateTime payTime; // 支付时间
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间

    private String contactPhone; // 联系电话
    private BigDecimal totalAmount; // 总金额
    private List<OrderSeat> seats; // 座位号列表
}
