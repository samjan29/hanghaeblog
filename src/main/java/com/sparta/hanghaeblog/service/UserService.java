package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.MessageDto;
import com.sparta.hanghaeblog.dto.UserRequestDto;
import com.sparta.hanghaeblog.entity.User;
import com.sparta.hanghaeblog.entity.UserRoleEnum;
import com.sparta.hanghaeblog.exception.CustomException;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.exception.ErrorResponse;
import com.sparta.hanghaeblog.jwt.JwtUtil;
import com.sparta.hanghaeblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<?> signUp(UserRequestDto requestDto) {

        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.DUPLICATE_USERNAME));
        }

        UserRoleEnum role = UserRoleEnum.USER;

        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.WRONG_ADMIN_TOKEN));
            }
            role = UserRoleEnum.ADMIN;
        }

        userRepository.save(new User(requestDto.getUsername(), requestDto.getPassword(), role));

        return ResponseEntity.ok(new MessageDto("가입 성공"));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> login(UserRequestDto requestDto, HttpServletResponse response) {    // 이 부분 Response라는 것
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_MEMBER)
        );

        if (!user.getPassword().equals(requestDto.getPassword())) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NON_EXISTENT_MEMBER));
        }
        
        // 헤더에 등록
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return ResponseEntity.ok(new MessageDto("로그인 성공"));
    }
}
