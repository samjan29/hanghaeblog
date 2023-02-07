package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BlogResponseDto {
    private String title;
    private String contents;
    private String author;
    private String createdAt;


    public BlogResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt().toString().replace("T", " T").substring(0, 20);
    }
}
