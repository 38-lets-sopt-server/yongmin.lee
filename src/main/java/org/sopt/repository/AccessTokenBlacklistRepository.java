package org.sopt.repository;

import org.sopt.domain.AccessTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenBlacklistRepository extends JpaRepository<AccessTokenBlacklist, Long> {

    // Access Token이 블랙리스트에 있는지 확인
    boolean existsByToken(String token);
}
