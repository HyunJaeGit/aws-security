package io.github.hyunjaegit.aws_secutiry.filter;

import io.github.hyunjaegit.aws_secutiry.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // 👈 import 추가
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
// JWT 인증 필터 생성
// 이 클래스는 모든 HTTP 요청이 서버에 도달할 때마다 실행되어,
// 요청 헤더에 토큰이 있는지 확인하고 유효하다면 인증 객체를 Security Context에 넣어주는 역할
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 1. 요청 헤더에서 JWT 토큰을 추출하는 헬퍼 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer "를 제외한 토큰 부분만 반환
        }
        return null;
    }

    // 2. 필터 실행 로직
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request); // 토큰 추출

        // 토큰이 존재하고 유효한 경우, 인증 처리 진행
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 10단계에서 추가한 메서드를 이용해 토큰에서 사용자 이름(ID)을 추출
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // 인증 객체 (Authentication) 생성
            // 현재는 역할(Authorities)이 null이지만, 나중에는 UserDetailsService를 통해 권한을 로드해야 합니다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    null // 권한 정보가 없으므로 null로 설정
            );

            // 이 인증 객체를 SecurityContext에 저장하여 "인증된 사용자"로 처리합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터 또는 최종 목적지(Controller)로 요청 전달
        filterChain.doFilter(request, response);
    }
}