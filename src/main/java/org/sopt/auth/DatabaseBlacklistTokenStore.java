package org.sopt.auth;

import org.sopt.domain.AccessTokenBlacklist;
import org.sopt.repository.AccessTokenBlacklistRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// DB기반 블랙리스트 구현체
@Component
public class DatabaseBlacklistTokenStore implements BlacklistTokenStore{

    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;

    public DatabaseBlacklistTokenStore(AccessTokenBlacklistRepository accessTokenBlacklistRepository) {
        this.accessTokenBlacklistRepository = accessTokenBlacklistRepository;
    }


    @Override
    public void save(String accessToken, LocalDateTime expiresAt) {
        AccessTokenBlacklist blacklistToken = AccessTokenBlacklist.of(accessToken, expiresAt);

        accessTokenBlacklistRepository.save(blacklistToken);
    }

    @Override
    public boolean exists(String accessToken) {
        return accessTokenBlacklistRepository.existsByToken(accessToken);
    }
}
