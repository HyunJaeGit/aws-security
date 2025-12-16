package io.github.hyunjaegit.aws_secutiry.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}