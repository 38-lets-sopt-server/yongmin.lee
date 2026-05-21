package org.sopt.dto.auth.response;

public record ReissueTokenResponse(
        String accessToken,
        String refreshToken
) {
}
