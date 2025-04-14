package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private Integer id; // 主键ID
    private Integer userId; // 关联users表的id
    private String nickname = "神秘来客"; // 用户昵称
    private Integer gender; // 性别:0-未知 1-男 2-女
    private String email; // 邮箱
    private LocalDate birthday; // 出生日期
    private String avatar; // 头像URL
    private String city; // 城市
    private String province; // 省份
    private String signature = "话语不能透过纯白之门"; // 个人签名
    private String phone; // 手机号
    private String realName; // 真实姓名
    private String idCard; // 身份证号
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
