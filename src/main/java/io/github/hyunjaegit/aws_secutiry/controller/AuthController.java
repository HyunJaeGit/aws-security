package io.github.hyunjaegit.aws_secutiry.controller;

import io.github.hyunjaegit.aws_secutiry.dto.LoginRequest;
import io.github.hyunjaegit.aws_secutiry.dto.TokenResponse;
import io.github.hyunjaegit.aws_secutiry.util.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // 생성자 주입 (DI)
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest loginRequest) {

        // 1. Authentication 객체 생성 시도
        // (Spring Security의 UserDetailsService가 메모리 사용자(admin) 정보를 조회하고,
        // PasswordEncoder가 비밀번호를 비교하는 모든 과정을 수행합니다.)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. 인증 성공 시, JWT 토큰 생성 및 반환
        String token = jwtTokenProvider.generateToken(authentication.getName());

        return TokenResponse.builder()
                .token(token)
                .expiresIn(30 * 60L) // 30분 (30분 * 60초)
                .build();
    }
}