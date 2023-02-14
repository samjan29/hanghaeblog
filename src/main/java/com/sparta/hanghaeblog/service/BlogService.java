package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiResultEnum;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.BlogDto;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.BlogRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ApiUtils<?> getPosts() {
        List<Post> list = blogRepository.findAllByOrderByCreatedAtDesc();

        if (list.size() == 0) {
            return new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "작성된 글이 없음"));
        }

        List<BlogDto.Response> responseDtoList = new ArrayList<>();

        for (Post post : list) {
            Optional<User> optionalUser = userRepository.findById(post.getUserId());

            if (optionalUser.isPresent()) {
                responseDtoList.add(new BlogDto.Response(post, optionalUser.get().getUsername()));
            } else {
                responseDtoList.add(new BlogDto.Response(post, "no name"));
            }
        }

        return new ApiUtils<>(ApiResultEnum.SUCCESS, responseDtoList);
    }

    @Transactional
    public ApiUtils<?> createPost(BlogDto.Request requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 user 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
            }

            // 토큰에서 가져온 user 정보 조회
            Optional<User> optionalUser = userRepository.findByUsername(claims.getSubject());

            if (optionalUser.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
            }

            // 게시글 생성
            Post post = blogRepository.saveAndFlush(new Post(requestDto, optionalUser.get().getId()));

            return new ApiUtils<>(ApiResultEnum.SUCCESS, new BlogDto.Response(post, optionalUser.get().getUsername()));
        }

        return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(403, "접근 권한 없음"));
    }

    @Transactional(readOnly = true)
    public ApiUtils<?> getPost(Long id) {
        Optional<Post> optionalPost = blogRepository.findById(id);

        if(optionalPost.isEmpty()) {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
        }

        Optional<User> optionalUser = userRepository.findById(optionalPost.get().getUserId());

        if (optionalUser.isEmpty()) {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
        }

        return new ApiUtils<>(ApiResultEnum.SUCCESS, new BlogDto.Response(optionalPost.get(), optionalUser.get().getUsername()));
    }

    @Transactional
    public ApiUtils<?> updatePost(Long id, BlogDto.Request requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 user 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
            }

            // 토큰에서 가져 온 use 정보 조회
            Optional<User> optionalUser = userRepository.findByUsername(claims.getSubject());

            if (optionalUser.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
            }

            // post 정보 조회
            Optional<Post> optionalPost = blogRepository.findById(id);

            if (optionalPost.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
            }

            // 게시글 생성
            optionalPost.get().update(requestDto);

            return new ApiUtils<>(ApiResultEnum.SUCCESS, new BlogDto.Response(optionalPost.get(), optionalUser.get().getUsername()));
        }

        return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(403, "접근 권한 없음"));
    }

    @Transactional
    public ApiUtils<ApiMessage> deletePost(Long id, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 user 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
            }

            // 토큰에서 가져 온 use 정보 조회
            Optional<User> optionalUser = userRepository.findByUsername(claims.getSubject());

            if (optionalUser.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
            }

            // post 정보 조회
            Optional<Post> optionalPost = blogRepository.findById(id);

            if (optionalPost.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
            }


            blogRepository.deleteById(id);
            return new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "게시글 삭제"));
        }

        return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(403, "접근 권한 없음"));
    }
}
