package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id; // 主键ID
    private String username; // 用户名
    private String passwordHash; // 加密后的密码
    private Boolean isAdmin = false; // 管理员标识：false-普通用户, true-管理员
    private Boolean isBlocked = false; // 拉黑标识：false-正常, true-已拉黑
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
