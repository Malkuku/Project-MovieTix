package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {
    private Integer id; // 提醒ID
    private Integer userId; // 用户ID
    private Integer screeningId; // 场次ID
    private LocalDateTime notifyTime; // 应提醒时间
    private Boolean isNotified = false; // 是否已提醒:0-否 1-是
    private LocalDateTime notifiedAt; // 实际提醒时间
    private LocalDateTime createdAt; // 创建时间
}