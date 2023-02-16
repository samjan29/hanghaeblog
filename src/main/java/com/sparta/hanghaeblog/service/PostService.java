package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiResultEnum;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.PostDto;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.exception.CustomException;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.exception.ErrorResponse;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.PostRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public ApiUtils<?> getPosts() {
        List<Post> list = postRepository.findAllByOrderByCreatedAtDesc();

        if (list.size() == 0) {
            return new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "작성된 글이 없음"));
        }

        List<PostDto.Response> responseDtoList = new ArrayList<>();

        for (Post post : list) {
            Optional<User> userOptional = userRepository.findById(post.getUser().getId());

            if (userOptional.isPresent()) {
                responseDtoList.add(new PostDto.Response(post, userOptional.get().getUsername()));
            } else {
                responseDtoList.add(new PostDto.Response(post, "empty user")); // 탈퇴 회원의 글인 경우
            }
        }

        return new ApiUtils<>(ApiResultEnum.SUCCESS, responseDtoList);
    }

    @Transactional
    public ResponseEntity<?> createPost(PostDto.Request requestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 토큰에서 가져온 user 정보 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
            );

            // 게시글 생성
            Post post = postRepository.saveAndFlush(new Post(requestDto, user));

            return ResponseEntity.ok(new ApiUtils<>(ApiResultEnum.SUCCESS, new PostDto.Response(post, user.getUsername())));
        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN));
        }
    }

    @Transactional(readOnly = true)
    public ApiUtils<?> getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        // 탈퇴 회원
        if (userRepository.findById(post.getUser().getId()).isEmpty()) {
            return new ApiUtils<>(ApiResultEnum.SUCCESS, new PostDto.Response(post, "empty user"));
        }

        return new ApiUtils<>(ApiResultEnum.SUCCESS, new PostDto.Response(post, post.getUser().getUsername()));
    }

    @Transactional
    public ResponseEntity<?> updatePost(Long id, PostDto.Request requestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 토큰에서 가져 온 use 정보 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
            );

            // post 정보 조회
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.EMPTY_DATA)
            );


            if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                post.update(requestDto);

                return ResponseEntity.ok(new ApiUtils<>(ApiResultEnum.SUCCESS, new PostDto.Response(post, post.getUser().getUsername())));
            } else {
                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY));
            }

        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN));
        }
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long id, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 토큰에서 가져 온 use 정보 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
            );

            // post 정보 조회
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.EMPTY_DATA)
            );

            if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                postRepository.deleteById(id);

                return ResponseEntity.ok(new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "삭제 성공")));
            } else {
                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY));
            }

        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN));
        }
    }
}
