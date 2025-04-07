package com.movietix.xiazihao.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryParam {
    private String username; // 用户名模糊查询
    private Integer isAdmin; // 管理员标识：0-普通用户,1-管理员
    private Integer isBlocked; // 拉黑标识：0-正常,1-已拉黑
    private LocalDateTime createdAtFrom;// 创建时间范围起始
    private LocalDateTime createdAtTo;// 创建时间范围结束
    private Integer page = 1;       // 页码(默认1)
    private Integer pageSize = 10;  // 每页条数(默认10)
}
