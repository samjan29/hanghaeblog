package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.PostDto;
import com.sparta.hanghaeblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> createPost(@RequestBody PostDto.Request requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    @GetMapping("/post")
    public PostDto.Response getPost(@RequestParam Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePutPost(@PathVariable Long id, @RequestBody PostDto.Request requestDto, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, request);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }
}