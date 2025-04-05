package com.movietix.xiazihao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
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
