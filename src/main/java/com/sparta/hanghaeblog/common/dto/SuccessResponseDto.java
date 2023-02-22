package com.sparta.hanghaeblog.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class SuccessResponseDto<T> {
    private final int status = 200;
    private final T result;

    public SuccessResponseDto(T result) {
        this.result = result;
    }
}
