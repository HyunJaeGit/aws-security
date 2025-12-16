package io.github.hyunjaegit.aws_secutiry.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn;
}