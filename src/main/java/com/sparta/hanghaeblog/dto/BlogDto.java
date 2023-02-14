package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BlogDto {
    @Getter
    public static class Request {
        private String title;
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
