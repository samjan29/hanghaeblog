package com.sparta.hanghaeblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    NO_AUTHORITY(BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    DUPLICATE_USERNAME(BAD_REQUEST, "중복된 username 입니다."),
    NON_EXISTENT_MEMBER(BAD_REQUEST, "회원을 찾을 수 없습니다."),
    WRONG_ADMIN_TOKEN(BAD_REQUEST, "잘못된 관리자 번호입니다."),

    /* 401 : 인증 실패 */
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    NULL_TOKEN(UNAUTHORIZED, "토큰이 없습니다."),

    /* 403 FORBIDDEN : 인가 실패 */
    PERMISSION_DINED(FORBIDDEN, "권한이 없습니다."),

    /* 404 NOT_FOUND : 존재하지 않는 페이지 */
    EMPTY_DATA(NOT_FOUND, "게시글/댓글이 존재하지 않습니다."),

    /* 418 I_AM_A_TEAPOT : 존재하지 않는 페이지 */
    TEAPOT(I_AM_A_TEAPOT, "나는 찻주전자!");

    private final HttpStatus httpStatus;
    private final String detail;
}
