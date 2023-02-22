package com.sparta.hanghaeblog.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class UserRequestDto {
    @NotBlank(message = "입력되지 않았습니다.")
    @Pattern(regexp = "[a-z\\d]{4,10}", message = "길이가 다르거나 패턴이 틀렸습니다.")
    private String username;
    @NotBlank(message = "입력되지 않았습니다.")
    @Pattern(regexp = "[a-zA-Z\\d\\W]{8,15}", message = "길이가 다르거나 패턴이 틀렸습니다.")
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}
