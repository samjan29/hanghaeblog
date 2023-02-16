package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.MessageDto;
import com.sparta.hanghaeblog.dto.CommentDto;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.exception.CustomException;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.exception.ErrorResponse;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.CommentRepository;
import com.sparta.hanghaeblog.repository.PostRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> createComment(Long postId, CommentDto.Request commentRequestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 댓글을 쓸 유저가 존재하는지
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
            );

            // 게시글이 존재하는지
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new CustomException(ErrorCode.EMPTY_DATA)
            );

            Comment comment = commentRepository.save(new Comment(commentRequestDto, post, user));

            return ResponseEntity.ok(new CommentDto.Response(comment));
        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN));
        }
    }

    public ResponseEntity<?> updateComment(Long postId, Long id, CommentDto.Request commentRequestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 댓글을 쓸 유저가 존재하는지
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
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
            if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                comment.update(commentRequestDto);

                return ResponseEntity.ok(new CommentDto.Response(comment));
            } else {
                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY));
            }

        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN));
        }
    }

    public ResponseEntity<?> deleteComment(Long postId, Long id, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 댓글을 쓸 유저가 존재하는지
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
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
            if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                commentRepository.deleteById(id);

                return ResponseEntity.ok(new MessageDto("삭제 성공"));
            } else {
                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NO_AUTHORITY));
            }

        } else {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.INVALID_TOKEN));
        }
    }
}
