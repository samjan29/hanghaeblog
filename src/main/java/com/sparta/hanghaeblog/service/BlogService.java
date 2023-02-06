package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.BlogRequestDto;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return blogRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional
    public Post createPost(BlogRequestDto requestDto) {
        Post post = new Post(requestDto);
        blogRepository.save(post);
        return post;
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPost(Long id) {
        return blogRepository.findById(id);
    }

    @Transactional
    public Long updatePost(Long id, BlogRequestDto requestDto) {
        Post post = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        post.update(requestDto);
        return post.getId();
    }

    @Transactional
    public Long deletePost(Long id) {
        blogRepository.deleteById(id);
        return id;
    }
}
