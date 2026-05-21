package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    private String providerId;

    protected User(){}

    private User(String nickname, String email, String password, AuthProvider provider, String providerId) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
    }

    // 이메일/비밀번호 기반 회원 생성
    public static User local(String nickname, String email, String encodedPassword) {
        return new User(
                nickname,
                email,
                encodedPassword,
                AuthProvider.LOCAL,
                null
        );
    }

    // 카카오 OAuth 기반 회원 생성
    public static User kakao(String nickname, String email, String providerId) {
        return new User(
                nickname,
                email,
                null,
                AuthProvider.KAKAO,
                providerId
        );
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }
}
