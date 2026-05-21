package org.sopt.dto.auth.response;

public record SignupResponse(
        Long userId,
        String nickname,
        String email
) {
}
