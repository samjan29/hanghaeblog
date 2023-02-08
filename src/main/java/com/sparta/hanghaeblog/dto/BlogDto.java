package com.sparta.hanghaeblog.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlogDto<T> {
    private final String result;
    private final T data;
}
