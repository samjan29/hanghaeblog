package com.sparta.hanghaeblog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@RequiredArgsConstructor
public enum SuccessStatusCode {
    /* 200 : 요청 성공 */
    NO_AUTHORITY(OK, "작성자만 삭제/수정할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
