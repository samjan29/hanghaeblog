package com.sparta.hanghaeblog.domain.comment.service;

import com.sparta.hanghaeblog.domain.comment.entity.CommentLike;
import com.sparta.hanghaeblog.domain.comment.repository.CommentLikeRepository;
import com.sparta.hanghaeblog.common.dto.SuccessResponseDto;
import com.sparta.hanghaeblog.domain.comment.dto.CommentDto;
import com.sparta.hanghaeblog.domain.comment.entity.Comment;
import com.sparta.hanghaeblog.domain.comment.repository.CommentRepository;
import com.sparta.hanghaeblog.domain.post.entity.Post;
import com.sparta.hanghaeblog.domain.user.entity.User;
import com.sparta.hanghaeblog.exception.CustomException;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.exception.ErrorResponse;
import com.sparta.hanghaeblog.domain.post.repository.PostRepository;
import com.sparta.hanghaeblog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    public ResponseEntity<SuccessResponseDto<CommentDto.Response>> createComment(Long postId, CommentDto.Request commentRequestDto, User user) {
        // 게시글이 존재하는지
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        Comment comment = commentRepository.save(new Comment(commentRequestDto, post, user));

        return ResponseEntity.ok(new SuccessResponseDto<>(new CommentDto.Response(comment, 0L)));
    }

    public ResponseEntity<?> updateComment(Long postId, Long id, CommentDto.Request commentRequestDto, User user) {
        // 게시글이 존재하는지
        postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        // 댓글이 존재하는지
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        // 댓글을 쓴 유저와 동일한지
        if (comment.getUser().getUsername().equals(user.getUsername())) {
            comment.update(commentRequestDto);

            Long like = commentLikeRepository.countByCommentIdAndIsShow(id, 1);

            return ResponseEntity.ok(new CommentDto.Response(comment, like));
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.PERMISSION_DINED);
        }
    }

    public ResponseEntity<?> deleteComment(Long postId, Long id, User user) {
        // 게시글이 존재하는지
        postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        // 댓글이 존재하는지
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        // 댓글을 쓴 유저와 동일한지
        if (comment.getUser().getUsername().equals(user.getUsername())) {
            commentRepository.deleteById(id);

            return ResponseEntity.ok(new SuccessResponseDto<>("댓글 삭제 성공"));
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.PERMISSION_DINED);
        }
    }

    public ResponseEntity<?> toggleLike(Long postId, Long id, User user) {
        // 게시글이 존재하는지
        postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        // 댓글이 존재하는지
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        Optional<CommentLike> commentLikeOptional = commentLikeRepository.findByCommentIdAndUserId(id, user.getId());
        if (commentLikeOptional.isPresent()) {
            CommentLike commentLike = commentLikeOptional.get();

            if (commentLike.getIsShow() == 1) {
                commentLike.toggle(0);
                return ResponseEntity.ok(new SuccessResponseDto<>("안 좋아요"));
            } else {
                commentLike.toggle(1);
            }
        } else {
            commentLikeRepository.save(new CommentLike(comment, user));
        }

        return ResponseEntity.ok(new SuccessResponseDto<>("좋아요"));
    }
}
