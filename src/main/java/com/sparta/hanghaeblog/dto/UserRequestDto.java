package com.sparta.hanghaeblog.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class UserRequestDto {
    @NotBlank(message = "ID: 입력되지 않음")
    @Size(min = 4, max = 10, message = "ID: 범위를 벗어남")
    @Pattern(regexp = "[a-z\\d]{4,10}", message = "ID: 패턴 틀림")
    private String username;
    @NotBlank(message = "PW: 입력되지 않음")
    @Size(min = 8, max = 15, message = "PW: 범위를 벗어남")
    @Pattern(regexp = "[a-zA-Z\\d]{8,15}", message = "PW: 패턴 틀림")
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}
