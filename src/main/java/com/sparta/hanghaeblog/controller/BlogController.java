package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BlogRequestDto;
import com.sparta.hanghaeblog.dto.BlogResponseDto;
import com.sparta.hanghaeblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/api/posts")
    public List<BlogResponseDto> getPosts() {
        return blogService.getPosts();
    }

    @PostMapping("/api/post")
    public BlogResponseDto createPost(@RequestBody BlogRequestDto requestDto) {
        return blogService.createPost(requestDto);
    }

    @GetMapping("/api/post/{id}")
    public BlogResponseDto getPost(@PathVariable Long id) {
        return blogService.getPost(id);
    }

    @PutMapping("/api/post/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.updatePost(id, requestDto);
    }

    @DeleteMapping("/api/post/{id}")
    public Long deletePost(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.deletePost(id, requestDto.getPassword());
    }
}
