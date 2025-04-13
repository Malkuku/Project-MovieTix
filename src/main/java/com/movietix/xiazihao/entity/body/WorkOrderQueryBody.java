package com.movietix.xiazihao.entity.body;

import com.movietix.xiazihao.entity.pojo.OrderSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderQueryBody {
    private Integer screeningId;
    private Integer userId;
    private String contactPhone;
    private List<OrderSeat> seats;
}