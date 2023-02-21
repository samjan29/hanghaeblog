package com.sparta.hanghaeblog.service;

import com.sparta.hanghaeblog.dto.SuccessResponseDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<?> signUp(UserRequestDto userRequestDto) {

        // 회원 중복 확인
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.DUPLICATE_USERNAME));
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;

        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.WRONG_ADMIN_TOKEN));
            }
            role = UserRoleEnum.ADMIN;
        }

        userRepository.save(new User(userRequestDto.getUsername(), passwordEncoder.encode(userRequestDto.getPassword()), role));

        return ResponseEntity.ok(new SuccessResponseDto<>(200, "회원 가입 성공"));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> login(UserRequestDto userRequestDto, HttpServletResponse response) {    // 이 부분 Response라는 것

        // 사용자 확인
        Optional<User> userOptional = userRepository.findByUsername(userRequestDto.getUsername());
        if (userOptional.isEmpty()) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NON_EXISTENT_MEMBER));
        }

        User user = userOptional.get();

        // 비밀번호 확인
        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            return ErrorResponse.toResponseEntity(new CustomException(ErrorCode.NON_EXISTENT_MEMBER));
        }

        // 헤더에 등록
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return ResponseEntity.ok(new SuccessResponseDto<>(200, "로그인 성공"));
    }
}
