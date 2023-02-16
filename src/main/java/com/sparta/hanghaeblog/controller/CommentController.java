package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.CommentDto;
import com.sparta.hanghaeblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentDto.Request commentRequestDto, HttpServletRequest request) {
        return commentService.createComment(postId, commentRequestDto, request);
    }

    @PutMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentDto.Request commentRequestDto, HttpServletRequest request) {
        return commentService.updateComment(postId, id, commentRequestDto, request);
    }

    @DeleteMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(postId, id, request);
    }
}
