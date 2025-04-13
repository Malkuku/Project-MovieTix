package com.movietix.xiazihao.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderQueryParam {
    private Integer id;
    private String movieTitle;
    private String hallName;
    private LocalDateTime startTime;
    private Integer status;
}
