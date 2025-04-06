package com.movietix.xiazihao.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningQueryParam {
        private Integer movieId;          // 电影ID
        private Integer hallId;          // 放映厅ID
        private String movieTitle;       // 电影名称(模糊查询)
        private String hallName;         // 放映厅名称(模糊查询)
        private LocalDateTime startTimeFrom; // 开始时间范围起始
        private LocalDateTime startTimeTo;  // 开始时间范围结束
        private BigDecimal minPrice;     // 最低票价
        private BigDecimal maxPrice;     // 最高票价
        private Integer minSeats;        // 最少剩余座位数
        private Integer status;          // 状态(0-已取消 1-正常)
        private Integer page = 1;       // 页码(默认1)
        private Integer pageSize = 10;  // 每页条数(默认10)
}
