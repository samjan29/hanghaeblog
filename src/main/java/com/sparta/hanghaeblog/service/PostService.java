package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.SuccessResponseDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<?> getPosts() {
        List<Post> list = postRepository.findAllByOrderByCreatedAtDesc();

        if (list.size() == 0) {
            return ResponseEntity.ok(new SuccessResponseDto<>(200, "아직 작성된 글이 없습니다."));
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

        return ResponseEntity.ok(new SuccessResponseDto<>(200, responseDtoList));
    }

    @Transactional
    public ResponseEntity<?> createPost(PostDto.Request requestDto, User user) {
        // 토큰에서 가져온 user 정보 조회
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isEmpty()) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NON_EXISTENT_MEMBER));
        }

        // 게시글 생성
        Post post = postRepository.save(new Post(requestDto, user));

        return ResponseEntity.ok(new SuccessResponseDto<>(200, new PostDto.Response(post, user.getUsername())));

    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.EMPTY_DATA));
        }

        Post post = postOptional.get();

        // 탈퇴 회원
        if (userRepository.findById(post.getUser().getId()).isEmpty()) {
            return ResponseEntity.ok(new SuccessResponseDto<>(200, new PostDto.Response(post, "empty user")));
        }

        return ResponseEntity.ok(new SuccessResponseDto<>(200, new PostDto.Response(post, post.getUser().getUsername())));
    }

    @Transactional
    public ResponseEntity<?> updatePost(Long id, PostDto.Request requestDto, User user) {
        // 토큰에서 가져 온 use 정보 조회
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isEmpty()) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NON_EXISTENT_MEMBER));
        }

        // post 정보 조회
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.EMPTY_DATA));
        }

        Post post = postOptional.get();


        if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            post.update(requestDto);

            return ResponseEntity.ok(new PostDto.Response(post, post.getUser().getUsername()));
        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY));
        }
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long id, User user) {
        // 토큰에서 가져 온 use 정보 조회
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
        );

        // post 정보 조회
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            postRepository.deleteById(id);

            return ResponseEntity.ok(new SuccessResponseDto<>(200, "게시글 삭제 성공"));
        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY));
        }
    }
}
