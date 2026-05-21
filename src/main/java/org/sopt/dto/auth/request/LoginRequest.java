package org.sopt.dto.auth.request;

public record LoginRequest(
        String email,
        String password
) {
}
