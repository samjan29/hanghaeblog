package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiResultEnum;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.UserRequestDto;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ApiUtils<ApiMessage> signUp(UserRequestDto requestDto) {
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "중복 ID"));
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "잘못된 관리자 암호"));
            }
            role = UserRoleEnum.ADMIN;
        }

        userRepository.save(new User(requestDto.getUsername(), requestDto.getPassword(), role));

        return new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "가입 성공"));
    }

    @Transactional(readOnly = true)
    public ApiUtils<ApiMessage> login(UserRequestDto requestDto, HttpServletResponse response) {    // 이 부분 Response라는 것
        Optional<User> userOptional = userRepository.findByUsername(requestDto.getUsername());
        if (userOptional.isEmpty()) {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "존재하지 않는 ID"));
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(requestDto.getPassword())) {
            return new ApiUtils<>(ApiResultEnum.FAILURE, new ApiMessage(500, "잘못된 비밀번호"));
        }
        
        // 헤더에 등록
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return new ApiUtils<>(ApiResultEnum.SUCCESS, new ApiMessage(200, "로그인 성공"));
    }
}
