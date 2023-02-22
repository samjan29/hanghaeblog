package com.sparta.hanghaeblog.dto;

import lombok.Getter;

@Getter
public class SuccessResponseDto<T> {
    private final int status = 200;
    private final T result;

    public SuccessResponseDto(T result) {
        this.result = result;
    }
}
