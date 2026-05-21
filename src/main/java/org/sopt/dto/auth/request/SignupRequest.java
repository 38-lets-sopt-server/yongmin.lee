package org.sopt.dto.auth.request;

public record SignupRequest(
        String nickname,
        String email,
        String password
) {
}
