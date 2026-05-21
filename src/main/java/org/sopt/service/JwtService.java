package org.sopt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    // JWT 서명 및 검증에 사용할 HMAC256 알고리즘
    private final Algorithm algorithm;

    // Access Token 유효 시간
    private final long accessTokenExpiresInSeconds;

    // Refresh Token 유효 시간
    private final long refreshTokenExpiresInSeconds;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-expires-in-seconds:1800}") long accessTokenExpiresInSeconds,
            @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}") long refreshTokenExpiresInSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.accessTokenExpiresInSeconds = accessTokenExpiresInSeconds;
        this.refreshTokenExpiresInSeconds = refreshTokenExpiresInSeconds;
    }

    // Access Token 생성
    public String generateAccessToken(Long userId, String email) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("email", email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(accessTokenExpiresInSeconds)))
                .sign(algorithm);
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long userId) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(refreshTokenExpiresInSeconds)))
                .sign(algorithm);
    }

    // JWT 검증 후 Subject에 저장된 유저 ID 반환
    public Long verifyAndGetUserId(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        try {
            return Long.parseLong(jwt.getSubject());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("JWT의 회원 정보가 올바르지 않습니다.");
        }
    }

    // Access Token의 만료 시각 추출(블랙리스트 저장용)
    public LocalDateTime getExpiration(String token){
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

        return LocalDateTime.ofInstant(
                jwt.getExpiresAt().toInstant(),
                ZoneId.systemDefault()
        );
    }

    // RefreshToken 생성 시 만료 시간 반환
    public long getRefreshTokenExpiresInSeconds(){
        return refreshTokenExpiresInSeconds;
    }
}
