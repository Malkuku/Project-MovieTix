package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id; // 主键ID
    private String username; // 用户名
    private String passwordHash; // 加密后的密码
    private Integer isAdmin = 0; // 管理员标识：0-普通用户, 1-管理员
    private Integer isBlocked = 0; // 拉黑标识：0-正常, 1-已拉黑
    private BigDecimal balance = BigDecimal.ZERO; // 账户余额
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间

    //token
    private String token; // 用户登录后生成的token
}
