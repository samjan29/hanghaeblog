package com.sparta.hanghaeblog.apiFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiMessage {
    private final int statusCode;
    private final String message;
}
