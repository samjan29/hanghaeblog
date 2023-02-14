package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.BlogDto;
import com.sparta.hanghaeblog.dto.UserRequestDto;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/posts")
    public ApiUtils<?> getPosts() {
        return blogService.getPosts();
    }

    @PostMapping("/post")
    public ApiUtils<?> createPost(@RequestBody BlogDto.Request requestDto, HttpServletRequest request) {
        return blogService.createPost(requestDto, request);
    }

    @GetMapping("/post")
    public ApiUtils<?> getPost(@RequestParam Long id) {
        return blogService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public ApiUtils<?> updatePutPost(@PathVariable Long id, @RequestBody BlogDto.Request requestDto, HttpServletRequest request) {
        return blogService.updatePost(id, requestDto, request);
    }

    @DeleteMapping("/post/{id}")
    public ApiUtils<ApiMessage> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return blogService.deletePost(id, request);
    }
}