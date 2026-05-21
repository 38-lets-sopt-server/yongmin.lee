package org.sopt.dto.auth.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
