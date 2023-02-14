package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Post;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class PostDto {
    @Getter
    public static class Request {
        @NotBlank(message = "TITLE: 입력되지 않음")
        private String title;
        @NotBlank(message = "CONTENTS: 입력되지 않음")
        private String contents;
    }

    @Getter
    public static class Response {
        private final Long id;
        private final String title;
        private final String contents;
        private final String username;
        private final String createdAt;

        public Response(Post post, String username) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.contents = post.getContents();
            this.username = username;
            this.createdAt = post.getCreatedAt().toString().replace("T", " T").substring(0, 20);
        }
    }
}
