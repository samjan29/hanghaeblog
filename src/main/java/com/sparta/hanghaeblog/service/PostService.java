package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.CommentDto;
import com.sparta.hanghaeblog.dto.SuccessResponseDto;
import com.sparta.hanghaeblog.dto.PostDto;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.exception.CustomException;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.exception.ErrorResponse;
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

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPosts() {
        List<Post> list = postRepository.findAllByOrderByCreatedAtDesc();

        if (list.size() == 0) {
            return ResponseEntity.ok(new SuccessResponseDto<>("아직 작성된 글이 없습니다."));
        }

        List<PostDto.Response> responseDtoList = new ArrayList<>();

        for (Post post : list) {
            Optional<User> userOptional = userRepository.findById(post.getUser().getId());

            ArrayList<CommentDto.Response> comments = new ArrayList<>();
            for (Comment comment : post.getCommentList()) {
                comments.add(new CommentDto.Response(comment));
            }

            if (userOptional.isPresent()) {
                responseDtoList.add(new PostDto.Response(post, userOptional.get().getUsername(), comments));
            } else {
                responseDtoList.add(new PostDto.Response(post, "empty user", comments)); // 탈퇴 회원의 글인 경우
            }
        }

        return ResponseEntity.ok(new SuccessResponseDto<>(responseDtoList));
    }

    @Transactional
    public ResponseEntity<?> createPost(PostDto.Request requestDto, User user) {
        // 토큰에서 가져온 user 정보 조회
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
        );

        // 게시글 생성
        Post post = postRepository.save(new Post(requestDto, user));

        ArrayList<CommentDto.Response> comments = new ArrayList<>();
        for (Comment comment : post.getCommentList()) {
            comments.add(new CommentDto.Response(comment));
        }

        return ResponseEntity.ok(new SuccessResponseDto<>(new PostDto.Response(post, user.getUsername(), comments)));

    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        ArrayList<CommentDto.Response> comments = new ArrayList<>();
        for (Comment comment : post.getCommentList()) {
            comments.add(new CommentDto.Response(comment));
        }

        // 탈퇴 회원
        if (userRepository.findById(post.getUser().getId()).isEmpty()) {
            return ResponseEntity.ok(new SuccessResponseDto<>(new PostDto.Response(post, "empty user", comments)));
        }

        return ResponseEntity.ok(new SuccessResponseDto<>(new PostDto.Response(post, post.getUser().getUsername(), comments)));
    }

    @Transactional
    public ResponseEntity<?> updatePost(Long id, PostDto.Request requestDto, User user) {
        // 토큰에서 가져 온 use 정보 조회
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
        );

        // post 정보 조회
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            post.update(requestDto);

            ArrayList<CommentDto.Response> comments = new ArrayList<>();
            for (Comment comment : post.getCommentList()) {
                comments.add(new CommentDto.Response(comment));
            }

            return ResponseEntity.ok(new PostDto.Response(post, post.getUser().getUsername(), comments));
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.NO_AUTHORITY);
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

            return ResponseEntity.ok(new SuccessResponseDto<>("게시글 삭제 성공"));
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.NO_AUTHORITY);
        }
    }
}
