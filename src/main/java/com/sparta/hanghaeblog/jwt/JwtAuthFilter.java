package com.sparta.hanghaeblog.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaeblog.exception.ErrorCode;
import com.sparta.hanghaeblog.security.SecurityExceptionDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

        // JWT 예외 핸들링
        if (token != null) {
            if (!jwtUtil.validateToken(token)) {
                jwtExceptionHandler(response, ErrorCode.INVALID_TOKEN);
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
//        } else if (request.getRequestURI().contains("/api/auth") || !request.getMethod().equals("GET")) {    // 이렇게 하지 않으면 JWT에서는 회원가입도 못하고 조회도 안됨
//            // return 빼고 throw를 해야 할 듯
//            return;
//        } else if (!request.getMethod().equals("GET")) {
//            jwtExceptionHandler(response, ErrorCode.NULL_TOKEN);
//            return;
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(errorCode.getHttpStatus().value(), errorCode.getDetail()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
