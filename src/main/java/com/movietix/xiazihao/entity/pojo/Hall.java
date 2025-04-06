package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hall {
    private Integer id; // 主键ID
    private String name; // 厅名
    private Integer capacity; // 座位容量
    private Integer rows; // 行数
    private Integer cols; // 列数
    private String facilities; // 设施(JSON)
    private Integer status = 1; // 状态:0-关停 1-启用
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
