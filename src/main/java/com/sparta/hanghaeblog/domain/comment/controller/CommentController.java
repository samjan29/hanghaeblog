package com.sparta.hanghaeblog.domain.comment.controller;

import com.sparta.hanghaeblog.domain.comment.dto.CommentDto;
import com.sparta.hanghaeblog.common.dto.SuccessResponseDto;
import com.sparta.hanghaeblog.domain.comment.service.CommentService;
import com.sparta.hanghaeblog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<SuccessResponseDto<CommentDto.Response>> createComment(@PathVariable Long postId, @RequestBody CommentDto.Request commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails.getUser());
    }

    @PutMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentDto.Request commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(postId, id, commentRequestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(postId, id, userDetails.getUser());
    }

    @PostMapping("/post/{postId}/comment/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.toggleLike(postId, id, userDetails.getUser());
    }
}
