package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BlogRequestDto;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/api/posts")
    public List<Post> getPosts() {
        return blogService.getPosts();
    }

    @PostMapping("/api/post")
    public Post createPost(@RequestBody BlogRequestDto requestDto) {
        return blogService.createPost(requestDto);
    }

    @GetMapping("/api/post/{id}")
    public Optional<Post> getPost(@PathVariable Long id) {
        return blogService.getPost(id);
    }

    @PutMapping("/api/post/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.updatePost(id, requestDto);
    }

    @DeleteMapping("/api/post/{id}")
    public Long deletePost(@PathVariable Long id) {
        return blogService.deletePost(id);
    }
}
