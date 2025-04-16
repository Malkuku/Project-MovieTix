package com.movietix.xiazihao.entity.body;

import com.movietix.xiazihao.entity.pojo.OrderSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkPaymentQueryBody {
    private Integer orderId;
    private Integer userId;
    private String contactPhone;
    private List<OrderSeat> seats;
}