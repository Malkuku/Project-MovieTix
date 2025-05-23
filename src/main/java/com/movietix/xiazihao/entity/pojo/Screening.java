package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Screening {
    private Integer id; // 主键ID
    private Integer movieId; // 电影ID
    private Integer hallId; // 放映厅ID
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime; // 结束时间
    private Double price; // 票价
    private Integer remainingSeats; // 剩余座位数
    private Integer status = 1; // 状态:0-已取消 1-正常
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    private String movieTitle; // 电影名称
    private String hallName; // 放映厅名称
}
