package com.sparta.hanghaeblog.domain.post.dto;

import com.sparta.hanghaeblog.domain.comment.dto.CommentDto;
import com.sparta.hanghaeblog.domain.post.entity.Post;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

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
        private final Long like;
        private final List<CommentDto.Response> comments;

        public Response(Post post, String username, Long like, List<CommentDto.Response> comments) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.contents = post.getContents();
            this.username = username;
            this.createdAt = post.getCreatedAt().toString().replace("T", " T").substring(0, 20);
            this.like = like;
            this.comments = comments;
        }
    }
}
