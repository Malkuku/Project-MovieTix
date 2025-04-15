package com.movietix.xiazihao.entity.result;

import com.movietix.xiazihao.entity.pojo.OrderSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkSeatResult {
    private Integer rows; // 总行数
    private Integer cols; // 总列数
    List<OrderSeat> seats;
}
