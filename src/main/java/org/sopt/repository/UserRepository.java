package org.sopt.repository;

import org.sopt.domain.AuthProvider;
import org.sopt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // 회원가입 시 중복인지 확인
    boolean existsByEmail(String email);

    // 소셜 로그인 제공자와 제공자별 사용자 ID로 유저 조회
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
