package com.sparta.hanghaeblog.dto;

import com.sparta.hanghaeblog.entity.Comment;
import lombok.Getter;

public class CommentDto {
    @Getter
    public static class Request {
        private String contents;
    }

    @Getter
    public static class Response {
        private final Long id;
        private final String contents;
        private final String username;
        private final String createdAt;

        public Response(Comment comment) {
            this.id = comment.getId();
            this.contents = comment.getContents();
            this.username = comment.getUser().getUsername();
            this.createdAt = comment.getCreatedAt().toString().replace("T", " T").substring(0, 20);
        }
    }
}
