package com.sparta.hanghaeblog.controller;

import com.sparta.hanghaeblog.apiFormat.ApiMessage;
import com.sparta.hanghaeblog.apiFormat.ApiUtils;
import com.sparta.hanghaeblog.dto.UserRequestDto;
import com.sparta.hanghaeblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/signup")
    public ApiUtils<ApiMessage> signUp(@RequestBody UserRequestDto RequestDto) {
        return userService.signUp(RequestDto);
    }

    @PostMapping("/auth/login")
    public ApiUtils<ApiMessage> login(@RequestBody UserRequestDto RequestDto, HttpServletResponse response) {
        return userService.login(RequestDto, response);
    }
}
