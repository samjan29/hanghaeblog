package com.sparta.hanghaeblog.dto;

import lombok.Getter;

@Getter
public class BlogRequestDto {
    private String title;
    private String contents;
    private String author;
    private String password;
}
