package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Post;
import lombok.Getter;

@Getter
public class BlogResponseDto {
    private String title;
    private String contents;
    private String author;

    public BlogResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.author = post.getAuthor();
    }
}
