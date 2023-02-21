package com.sparta.hanghaeblog.security;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecurityExceptionDto {
    private int status;
    private String message;

    public SecurityExceptionDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
