package com.bigbird.blog.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Article {

    private Long id;

    private String title;

    private String summary;

    private String content;

    private String author;

    private String tags;

    private Integer views;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
