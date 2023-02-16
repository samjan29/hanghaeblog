package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiResultEnum;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.CommentDto;
import com.sparta.hanghaeblog.entity.Comment;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.CommentRepository;
import com.sparta.hanghaeblog.repository.PostRepository;
import com.sparta.hanghaeblog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ApiUtils<?> createComment(Long postId, CommentDto.Request commentRequestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 댓글을 쓸 유저가 존재하는지
            Optional<User> userOptional = userRepository.findByUsername(claims.getSubject());

            if (userOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "존재하지 않는 ID"));
            }

            // 게시글이 존재하는지
            Optional<Post> postOptional = postRepository.findById(postId);

            if (postOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
            }

            Comment comment = commentRepository.save(new Comment(commentRequestDto, postOptional.get(), userOptional.get()));

            return new ApiUtils<>(ApiResultEnum.SUCCESS, new CommentDto.Response(comment));
        } else {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
        }
    }

    public ApiUtils<?> updateComment(Long postId, Long id, CommentDto.Request commentRequestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 댓글을 쓸 유저가 존재하는지
            Optional<User> userOptional = userRepository.findByUsername(claims.getSubject());

            if (userOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "존재하지 않는 ID"));
            }

            // 게시글이 존재하는지
            Optional<Post> postOptional = postRepository.findById(postId);

            if (postOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
            }

            // 댓글이 존재하는지
            Optional<Comment> commentOptional = commentRepository.findById(id);

            if (commentOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "댓글이 존재하지 않음"));
            }

            Comment comment = commentOptional.get();
            User user = userOptional.get();

            // 댓글을 쓴 유저와 동일한지
            if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                comment.update(commentRequestDto);

                return new ApiUtils<>(ApiResultEnum.SUCCESS, new CommentDto.Response(comment));
            } else {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "수정 권한이 없음"));
            }

        } else {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
        }
    }

    public ApiUtils<ApiMessage> deleteComment(Long postId, Long id, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 댓글을 쓸 유저가 존재하는지
            Optional<User> userOptional = userRepository.findByUsername(claims.getSubject());

            if (userOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "존재하지 않는 ID"));
            }

            // 게시글이 존재하는지
            Optional<Post> postOptional = postRepository.findById(postId);

            if (postOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
            }

            // 댓글이 존재하는지
            Optional<Comment> commentOptional = commentRepository.findById(id);

            if (commentOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "댓글이 존재하지 않음"));
            }

            User user = userOptional.get();

            // 댓글을 쓴 유저와 동일한지
            if (commentOptional.get().getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                commentRepository.deleteById(id);

                return new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "삭제 성공"));
            } else {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "삭제 권한이 없음"));
            }

        } else {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
        }
    }
}
