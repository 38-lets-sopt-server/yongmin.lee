package org.sopt.domain;


import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    protected RefreshToken(){

    }

    private RefreshToken(Long userId, String token, LocalDateTime expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    // Refresh Token 엔티티를 생성하는 정적 팩토리 메서드
    public static RefreshToken of(Long userId, String token, long expiresInSeconds) {
        return new RefreshToken(
                userId,
                token,
                LocalDateTime.now().plusSeconds(expiresInSeconds)
        );
    }

    // Refresh Token Rotation을 위해 토큰 값과 만료 시간을 갱신
    public void rotate(String newToken, long expiresInSeconds) {
        this.token = newToken;
        this.expiresAt = LocalDateTime.now().plusSeconds(expiresInSeconds);
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
