package com.movietix.xiazihao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id; // 主键ID
    private Integer movieId; // 电影ID
    private Integer userId; // 用户ID
    private String content; // 评论内容
    private Integer rating; // 评分(1-5)
    private Integer status = 1; // 状态:0-隐藏 1-显示
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
