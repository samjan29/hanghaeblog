package com.sparta.hanghaeblog.domain.user.controller;

import com.sparta.hanghaeblog.common.dto.SuccessResponseDto;
import com.sparta.hanghaeblog.domain.user.dto.UserRequestDto;
import com.sparta.hanghaeblog.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponseDto<String>> signUp(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.signUp(userRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto<String>> login(@RequestBody @Valid UserRequestDto userRequestDto, HttpServletResponse response) {
        return userService.login(userRequestDto, response);
    }
}
