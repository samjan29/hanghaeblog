package com.sparta.hanghaeblog.domain.post.service;

import com.sparta.hanghaeblog.domain.comment.dto.CommentDto;
import com.sparta.hanghaeblog.domain.comment.repository.CommentLikeRepository;
import com.sparta.hanghaeblog.common.dto.SuccessResponseDto;
import com.sparta.hanghaeblog.domain.post.dto.PostDto;
import com.sparta.hanghaeblog.domain.post.entity.Post;
import com.sparta.hanghaeblog.domain.post.entity.PostLike;
import com.sparta.hanghaeblog.domain.post.repository.PostLikeRepository;
import com.sparta.hanghaeblog.domain.post.repository.PostRepository;
import com.sparta.hanghaeblog.domain.comment.entity.Comment;
import com.sparta.hanghaeblog.domain.user.entity.User;
import com.sparta.hanghaeblog.exception.CustomException;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.exception.ErrorResponse;
import com.sparta.hanghaeblog.domain.user.entity.UserRoleEnum;
import com.sparta.hanghaeblog.domain.user.repository.UserRepository;
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
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<SuccessResponseDto<?>> getPosts() {
        List<Post> list = postRepository.findAllByOrderByCreatedAtDesc();

        if (list.size() == 0) {
            return ResponseEntity.ok(new SuccessResponseDto<>("아직 작성된 글이 없습니다."));
        }

        List<PostDto.Response> responseDtoList = new ArrayList<>();

        for (Post post : list) {
            Optional<User> userOptional = userRepository.findById(post.getUser().getId());

            ArrayList<CommentDto.Response> comments = new ArrayList<>();
            for (Comment comment : post.getCommentList()) {
                comments.add(new CommentDto.Response(comment, commentLikeRepository.countByCommentIdAndIsShow(comment.getId(), 1)));
            }

            Long like = postLikeRepository.countByPostIdAndIsShow(post.getId(), 1);

            if (userOptional.isPresent()) {
                responseDtoList.add(new PostDto.Response(post, userOptional.get().getUsername(), like, comments));
            } else {
                responseDtoList.add(new PostDto.Response(post, "empty user", like, comments)); // 탈퇴 회원의 글인 경우
            }
        }

        return ResponseEntity.ok(new SuccessResponseDto<>(responseDtoList));
    }

    @Transactional
    public ResponseEntity<SuccessResponseDto<PostDto.Response>> createPost(PostDto.Request requestDto, User user) {
        // 게시글 생성
        Post post = postRepository.save(new Post(requestDto, user));

        ArrayList<CommentDto.Response> comments = new ArrayList<>();

        return ResponseEntity.ok(new SuccessResponseDto<>(new PostDto.Response(post, user.getUsername(), 0L, comments)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<SuccessResponseDto<PostDto.Response>> getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        ArrayList<CommentDto.Response> comments = new ArrayList<>();
        for (Comment comment : post.getCommentList()) {
            comments.add(new CommentDto.Response(comment, commentLikeRepository.countByCommentIdAndIsShow(comment.getId(), 1)));
        }

        Long like = postLikeRepository.countByPostIdAndIsShow(id, 1);

        // 탈퇴 회원
        if (userRepository.findById(post.getUser().getId()).isEmpty()) {
            return ResponseEntity.ok(new SuccessResponseDto<>(new PostDto.Response(post, "empty user", like, comments)));
        }

        return ResponseEntity.ok(new SuccessResponseDto<>(new PostDto.Response(post, post.getUser().getUsername(), like, comments)));
    }

    @Transactional
    public ResponseEntity<?> updatePost(Long id, PostDto.Request requestDto, User user) {
        // post 정보 조회
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            post.update(requestDto);

            ArrayList<CommentDto.Response> comments = new ArrayList<>();
            for (Comment comment : post.getCommentList()) {
                comments.add(new CommentDto.Response(comment, commentLikeRepository.countByCommentIdAndIsShow(comment.getId(), 1)));
            }

            Long like = postLikeRepository.countByPostIdAndIsShow(id, 1);

            return ResponseEntity.ok(new SuccessResponseDto<>(new PostDto.Response(post, post.getUser().getUsername(), like, comments)));
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.PERMISSION_DINED);
        }
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long id, User user) {
        // post 정보 조회
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            postRepository.deleteById(id);

            return ResponseEntity.ok(new SuccessResponseDto<>("게시글 삭제 성공"));
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.PERMISSION_DINED);
        }
    }

    @Transactional
    public ResponseEntity<SuccessResponseDto<String>> toggleLike(Long id, User user) {
        // post 정보 조회
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        Optional<PostLike> postLikeOptional = postLikeRepository.findByPostIdAndUserId(id, user.getId());
        if (postLikeOptional.isPresent()) {
            PostLike postLike = postLikeOptional.get();

            if (postLike.getIsShow() == 1) {
                postLike.toggle(0);
                return ResponseEntity.ok(new SuccessResponseDto<>("안 좋아요"));
            } else {
                postLike.toggle(1);
            }
        } else {
            postLikeRepository.save(new PostLike(post, user));
        }

        return ResponseEntity.ok(new SuccessResponseDto<>("좋아요"));
    }
}
