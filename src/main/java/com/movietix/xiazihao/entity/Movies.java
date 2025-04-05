package com.movietix.xiazihao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movies {
    Integer id;
    String title;
    String poster_url;
    String release_date;
    Integer duration;
    String genre;
    Double rating;
    Integer status;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}
