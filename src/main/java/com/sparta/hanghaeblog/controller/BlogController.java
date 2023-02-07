package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.BlogRequestDto;
import com.sparta.hanghaeblog.dto.BlogResponseDto;
import com.sparta.hanghaeblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/posts")
    public List<BlogResponseDto> getPosts() {
        return blogService.getPosts();
    }

    @PostMapping("/post")
    public String createPost(@RequestBody BlogRequestDto requestDto) {
        return blogService.createPost(requestDto);
    }

    @GetMapping("/post")
    public BlogResponseDto getPost(@RequestParam Long id) {
        return blogService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public String updatePutPost(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.updatePost(id, requestDto);
    }

//    @PatchMapping("/post/{id}")
//    public Long updatePatchPost(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
//        return blogService.updatePost(id, requestDto);
//    }

    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.deletePost(id, requestDto.getPassword());
    }
}