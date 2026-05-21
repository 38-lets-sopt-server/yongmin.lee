package org.sopt.dto.auth.request;

public record ReissueTokenRequest(
        String refreshToken
) {
}
