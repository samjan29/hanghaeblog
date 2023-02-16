package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiResultEnum;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.CommentDto;
import com.sparta.hanghaeblog.dto.PostDto;
import com.sparta.hanghaeblog.entity.Post;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.PostRepository;
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
    public ApiUtils<?> createPost(PostDto.Request requestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 토큰에서 가져온 user 정보 조회
            Optional<User> userOptional = userRepository.findByUsername(claims.getSubject());

            if (userOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
            }

            // 게시글 생성
            Post post = postRepository.saveAndFlush(new Post(requestDto, userOptional.get()));

            return new ApiUtils<>(ApiResultEnum.SUCCESS, new PostDto.Response(post, userOptional.get().getUsername()));
        } else {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
        }
    }

    @Transactional(readOnly = true)
    public ApiUtils<?> getPost(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);

        if(postOptional.isEmpty()) {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
        }

        // 탈퇴한 회원의 글 조회가 가능하게 할 것인지
        if (userRepository.findById(postOptional.get().getUser().getId()).isEmpty()) {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
        }

        return new ApiUtils<>(ApiResultEnum.SUCCESS, new PostDto.Response(postOptional.get(), postOptional.get().getUser().getUsername()));
    }

    @Transactional
    public ApiUtils<?> updatePost(Long id, PostDto.Request requestDto, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 토큰에서 가져 온 use 정보 조회
            Optional<User> userOptional = userRepository.findByUsername(claims.getSubject());

            if (userOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
            }

            // post 정보 조회
            Optional<Post> postOptional = postRepository.findById(id);

            if (postOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
            }

            Post post = postOptional.get();
            User user = userOptional.get();

            if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                post.update(requestDto);

                return new ApiUtils<>(ApiResultEnum.SUCCESS, new PostDto.Response(post, post.getUser().getUsername()));
            } else {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "수정 권한 없음"));
            }

        } else {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
        }
    }

    @Transactional
    public ApiUtils<ApiMessage> deletePost(Long id, HttpServletRequest request) {
        if (jwtUtil.combo(request)) {
            Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.resolveToken(request));

            // 토큰에서 가져 온 use 정보 조회
            Optional<User> userOptional = userRepository.findByUsername(claims.getSubject());

            if (userOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "작성자 데이터 없음"));
            }

            // post 정보 조회
            Optional<Post> postOptional = postRepository.findById(id);

            if (postOptional.isEmpty()) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(404, "게시글이 존재하지 않음"));
            }

            Post post = postOptional.get();
            User user = userOptional.get();

            if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
                postRepository.deleteById(id);

                return new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "삭제 성공"));
            } else {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "삭제 권한 없음"));
            }

        } else {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(401, "Token Error"));
        }
    }
}
