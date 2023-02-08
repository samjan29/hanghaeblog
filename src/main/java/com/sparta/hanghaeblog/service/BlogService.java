package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.BlogDto;
import com.sparta.hanghaeblog.dto.BlogMessageDto;
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
    public BlogDto<?> getPosts() {
        List<Post> list = blogRepository.findAllByOrderByCreatedAtDesc();

        if (list.size() == 0) {
            return new BlogDto<BlogMessageDto>("failure", new BlogMessageDto("작성된 게시글이 없습니다."));
        }

        List<BlogResponseDto> responseDtoList = new ArrayList<>();

        for (Post post : list) {
            responseDtoList.add(new BlogResponseDto(post));
        }

        return new BlogDto<List<BlogResponseDto>>("success", responseDtoList);
    }

    @Transactional
    public BlogDto<BlogMessageDto> createPost(BlogRequestDto requestDto) {
        Post post = new Post(requestDto);
        blogRepository.save(post);
        return new BlogDto<BlogMessageDto>("success", new BlogMessageDto("게시글 작성 성공했습니다."));
    }

    @Transactional(readOnly = true)
    public BlogDto<?> getPost(Long id) {
        // DTO<T> 만들어서 Exception 핸들링 할 것
        Post post;

        try {
            post = blogRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
        } catch (IllegalArgumentException exception) {
            return new BlogDto<BlogMessageDto>("failure", new BlogMessageDto("게시글이 존재하지 않습니다."));
        }

        return new BlogDto<BlogResponseDto>("success", new BlogResponseDto(post));
    }

    @Transactional
    public BlogDto<BlogMessageDto> updatePost(Long id, BlogRequestDto requestDto) {
        // 비밀번호 확인보다 id를 먼저 조회해야 한다 생각해서 위로 올렸다.
        Post post;

        try {
            post = blogRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
        } catch (IllegalArgumentException exception) {
            return new BlogDto<BlogMessageDto>("failure", new BlogMessageDto("게시글이 존재하지 않습니다."));
        }

        if (!validatePassword(id, requestDto.getPassword())) {
            return new BlogDto<BlogMessageDto>("failure", new BlogMessageDto("비밀번호가 틀렸습니다."));
        }

        post.update(requestDto);
        return new BlogDto<BlogMessageDto>("success", new BlogMessageDto("게시글 변경 성공했습니다."));
    }

    @Transactional
    public BlogDto<BlogMessageDto> deletePost(Long id, String password) {
        // 비밀번호 확인보다 id를 먼저 조회해야 한다 생각해서 위로 올렸다.
        try {
            blogRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
        } catch (IllegalArgumentException exception) {
            return new BlogDto<BlogMessageDto>("failure", new BlogMessageDto("게시글이 존재하지 않습니다."));
        }

        if (!validatePassword(id, password)) {
            return new BlogDto<BlogMessageDto>("failure", new BlogMessageDto("비밀번호가 틀렸습니다."));
        }

        blogRepository.deleteById(id);
        return new BlogDto<BlogMessageDto>("success", new BlogMessageDto("게시글이 삭제되었습니다."));
    }

    private Boolean validatePassword(Long id, String password) {
        return password.equals(blogRepository.getReferenceById(id).getPassword());
    }
}
