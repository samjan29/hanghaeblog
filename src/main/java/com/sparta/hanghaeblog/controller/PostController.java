package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.PostDto;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.security.UserDetailsImpl;
import com.sparta.hanghaeblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts() {
        return postService.getPosts();
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostDto.Request requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    @GetMapping("/post")
    public ResponseEntity<?> getPost(@RequestParam Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostDto.Request requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, userDetails.getUser());
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/admin/post/{id}")
    public ResponseEntity<?> updatePostAdmin(@PathVariable Long id, @RequestBody PostDto.Request requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePostAdmin(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/admin/post/{id}")
    public ResponseEntity<?> deletePostAdmin(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePostAdmin(id, userDetails.getUser());
    }
}