package io.github.hyunjaegit.aws_secutiry.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // ⚠️ Secret Key는 외부에 노출되면 안 됩니다.
    // application.yml 파일에 정의된 키를 가져옵니다.
    @Value("${jwt.secret}")
    private String secretKey;

    // 토큰 만료 시간 (예: 30분)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    // Secret Key를 Base64 디코딩하여 Key 객체로 변환합니다.
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT Access Token을 생성합니다.
     * @param username 토큰에 담을 사용자 ID
     * @return 생성된 JWT 문자열
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .subject(username) // 토큰의 주체 (사용자 ID)
                .issuedAt(now)     // 토큰 발행 시간
                .expiration(expiryDate) // 토큰 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 서명 알고리즘과 키 설정
                .compact();
    }

    /**
     * 토큰의 유효성을 검증합니다.
     * @param token 검증할 JWT
     * @return 토큰이 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 토큰 파싱 중 오류 발생 (만료, 변조, 형식 오류 등)
            return false;
        }
    }
    /**
     * 토큰에서 사용자 이름을 추출합니다. (JWT Filter에서 사용)
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // subject는 토큰 생성 시 넣었던 username입니다.
    }

}