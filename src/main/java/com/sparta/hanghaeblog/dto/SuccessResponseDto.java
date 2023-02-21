package com.sparta.hanghaeblog.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SuccessResponseDto<T> {
    private final int status;
    private final T result;
}
