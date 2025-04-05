package com.movietix.xiazihao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private Integer id; // 主键ID
    private Integer userId; // 用户ID
    private String action; // 操作类型
    private String ipAddress; // IP地址
    private String userAgent; // 用户代理
    private String details; // 详细信息
    private LocalDateTime createdAt; // 创建时间
}