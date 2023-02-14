package com.sparta.hanghaeblog.dto;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UserRequestDto {
    @NotBlank
    @Min(4) @Max(10)
    @Pattern(regexp = "[a-z\\d]{4,10}")
    private String username;
    @NotBlank
    @Min(8) @Max(15)
    @Pattern(regexp = "[a-zA-Z\\d]{8,15}")
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}
