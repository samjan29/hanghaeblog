package com.sparta.hanghaeblog.apiFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiUtils<T> {
    private final ApiResultEnum result;
    private final T data;
}
