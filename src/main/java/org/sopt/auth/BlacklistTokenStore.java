package org.sopt.auth;

import java.time.LocalDateTime;

// redis로 확장할 수 있게 interface 적용
public interface BlacklistTokenStore {

    // Access Token을 블랙리스트에 저장
    void save(String accessToken, LocalDateTime expiresAt);

    // Access Token이 블랙리스트에 존재하는지 확인
    boolean exists(String accessToken);
}
