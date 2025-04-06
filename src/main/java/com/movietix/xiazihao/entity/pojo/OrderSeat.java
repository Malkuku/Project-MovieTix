package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSeat {
    private Integer id; // ID
    private Integer orderId; // 订单ID
    private Integer seatRow; // 座位行
    private Integer seatCol; // 座位列
    private String seatNo; // 座位号(如A01)
    private BigDecimal price; // 座位价格
}
