package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.CommentDto;
import com.sparta.hanghaeblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ApiUtils<?> createComment(@PathVariable Long postId, @RequestBody CommentDto.Request commentRequestDto, HttpServletRequest request) {
        return commentService.createComment(postId, commentRequestDto, request);
    }

    @PutMapping("/post/{postId}/comment/{id}")
    public ApiUtils<?> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentDto.Request commentRequestDto, HttpServletRequest request) {
        return commentService.updateComment(postId, id, commentRequestDto, request);
    }

    @DeleteMapping("/post/{postId}/comment/{id}")
    public ApiUtils<ApiMessage> deleteComment(@PathVariable Long postId, @PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(postId, id, request);
    }
}
