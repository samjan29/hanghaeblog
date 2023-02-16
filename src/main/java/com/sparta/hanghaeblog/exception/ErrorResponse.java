package com.sparta.hanghaeblog.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(exception.getErrorCode().getHttpStatus().value())
                        .message(exception.getErrorCode().getDetail())
                        .build());
    }
}
