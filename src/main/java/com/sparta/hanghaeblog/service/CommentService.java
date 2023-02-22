package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.SuccessResponseDto;
import com.sparta.hanghaeblog.dto.CommentDto;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.exception.CustomException;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.exception.ErrorResponse;
import com.sparta.hanghaeblog.repository.CommentRepository;
import com.sparta.hanghaeblog.repository.PostRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> createComment(Long postId, CommentDto.Request commentRequestDto, User user) {
        // 댓글을 쓸 유저가 존재하는지
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
        );

        // 게시글이 존재하는지
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.EMPTY_DATA)
        );

        Comment comment = commentRepository.save(new Comment(commentRequestDto, post, user));

        return ResponseEntity.ok(new CommentDto.Response(comment));
    }

    public ResponseEntity<?> updateComment(Long postId, Long id, CommentDto.Request commentRequestDto, User user) {
        // 댓글을 쓸 유저가 존재하는지
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
        );

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

            return ResponseEntity.ok(new CommentDto.Response(comment));
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.NO_AUTHORITY);
        }
    }

    public ResponseEntity<?> deleteComment(Long postId, Long id, User user) {
        // 댓글을 쓸 유저가 존재하는지
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
        );

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
            return ErrorResponse.toResponseEntity(ErrorCode.NO_AUTHORITY);
        }
    }
}
