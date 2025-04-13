package com.movietix.xiazihao.entity.result;

import com.movietix.xiazihao.entity.pojo.OrderSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderResult {
    private Integer orderId; // 订单ID
    private String orderNo; // 订单号
    private String movieTitle; // 电影名称
    private String hallName; // 影厅名称
    private LocalDateTime startTime; // 开始时间
    private Integer status; // 状态（0-待支付，1-已支付，2-已取消，3-已完成，4-已退款）
    private String contactPhone; // 联系电话
    private BigDecimal totalAmount; // 总金额
    private List<OrderSeat> seats; // 座位号列表
}
