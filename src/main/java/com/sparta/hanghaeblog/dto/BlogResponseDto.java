package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Post;
import lombok.Getter;

@Getter
public class BlogResponseDto {
    private final Long id;
    private final String title;
    private final String contents;
    private final String author;
    private final String createdAt;

    public BlogResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt().toString().replace("T", " T").substring(0, 20);
    }
}
