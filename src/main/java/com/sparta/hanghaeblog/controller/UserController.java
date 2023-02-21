package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.dto.UserRequestDto;
import com.sparta.hanghaeblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.signUp(userRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequestDto userRequestDto, HttpServletResponse response) {
        return userService.login(userRequestDto, response);
    }
}
