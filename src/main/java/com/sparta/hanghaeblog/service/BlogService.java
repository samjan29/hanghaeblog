package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.BlogRequestDto;
import com.sparta.hanghaeblog.dto.BlogResponseDto;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getPosts() {
        List<Post> list = blogRepository.findAllByOrderByModifiedAtDesc();
        List<BlogResponseDto> dtoList = new ArrayList<>();

        for (Post post : list) {
            dtoList.add(new BlogResponseDto(post));
        }

        return dtoList;
    }

    @Transactional
    public BlogResponseDto createPost(BlogRequestDto requestDto) {
        Post post = new Post(requestDto);
        blogRepository.save(post);
        return new BlogResponseDto(post);
    }

    @Transactional(readOnly = true)
    public BlogResponseDto getPost(Long id) {
        Post post = blogRepository.findById(id).orElseThrow(
                ()  -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        return new BlogResponseDto(post);
    }

    @Transactional
    public Long updatePost(Long id, BlogRequestDto requestDto) {
        // 비밀번호 확인보다 id를 먼저 조회해야 한다 생각해서 위로 올렸다.
        Post post = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        if (!validatePassword(id, requestDto.getPassword())) {
            return -99L;
        }

        post.update(requestDto);
        return post.getId();
    }

    @Transactional
    public Long deletePost(Long id, String password) {
        // 비밀번호 확인보다 id를 먼저 조회해야 한다 생각해서 위로 올렸다.
        blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        if (!validatePassword(id, password)) {
            return -99L;
        }

        blogRepository.deleteById(id);
        return id;
    }

    private Boolean validatePassword(Long id, String password) {
        return password.equals(blogRepository.getReferenceById(id).getPassword());
    }
}
