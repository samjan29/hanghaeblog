package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.PostDto;
import com.sparta.hanghaeblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public ApiUtils<?> getPosts() {
        return postService.getPosts();
    }

    @PostMapping("/post")
    public ApiUtils<?> createPost(@RequestBody PostDto.Request requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    @GetMapping("/post")
    public ApiUtils<?> getPost(@RequestParam Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public ApiUtils<?> updatePutPost(@PathVariable Long id, @RequestBody PostDto.Request requestDto, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, request);
    }

    @DeleteMapping("/post/{id}")
    public ApiUtils<ApiMessage> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }
}