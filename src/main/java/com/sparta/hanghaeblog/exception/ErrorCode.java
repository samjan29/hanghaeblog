package com.sparta.hanghaeblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_TOKEN(BAD_REQUEST, "토큰이 유효하지 않습니다."),
    NO_AUTHORITY(BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    DUPLICATE_USERNAME(BAD_REQUEST, "중복된 username 입니다."),
    NON_EXISTENT_MEMBER(BAD_REQUEST, "회원을 찾을 수 없습니다."),
    EMPTY_DATA(BAD_REQUEST, "게시글/댓글이 존재하지 않습니다."),
    WRONG_ADMIN_TOKEN(BAD_REQUEST, "잘못된 관리자 번호입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
