package com.movietix.xiazihao.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetail {
    private Integer id; // 主键ID
    private Integer movieId; // 电影ID
    private String description; // 详细描述
    private String director; // 导演
    private String actors; // 主演列表
    private String language; // 语言
    private String country; // 国家/地区
    private String trailerUrl; // 预告片URL
    private String awards; // 获奖情况
    private String producer; // 制片人
    private String screenwriter; // 编剧
    private String filmLocation; // 拍摄地
    private Double budget; // 预算
    private Double boxOffice; // 票房
    private String tags; // 标签
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
