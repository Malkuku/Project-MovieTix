package com.movietix.xiazihao.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 放映厅查询参数实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallQueryParam {
    private String name;         // 放映厅名称(模糊查询)
    private Integer minCapacity; // 最小座位容量
    private Integer maxCapacity; // 最大座位容量
    private Integer minRows;     // 最小行数
    private Integer minCols;     // 最小列数
    private String facility;     // 设施关键词(模糊查询)
    private Integer status;      // 状态(0-关停,1-启用)
    private Integer page = 1;    // 页码(默认1)
    private Integer pageSize = 10; // 每页条数(默认10)
}
