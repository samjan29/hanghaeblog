package com.sparta.hanghaeblog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaeblog.exception.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

//@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final SecurityExceptionDto exceptionDto = new SecurityExceptionDto(ErrorCode.PERMISSION_DINED.getHttpStatus().value(), ErrorCode.PERMISSION_DINED.getDetail());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(exceptionDto.getStatus());
        response.setContentType("application/json;charset=UTF-8");

        try (OutputStream os = response.getOutputStream()) {
            new ObjectMapper().writeValue(os, exceptionDto);
            os.flush();
        }
    }
}
