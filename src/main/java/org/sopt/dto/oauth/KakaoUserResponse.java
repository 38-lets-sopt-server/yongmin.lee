package org.sopt.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserResponse(

        // 카카오에서 내려주는 사용자 고유 ID
        Long id,

        // 카카오 계정 정보
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {

    public String getProviderId() {
        return String.valueOf(id);
    }

    public String getEmail() {
        return kakaoAccount.email();
    }

    public String getNickname() {
        return kakaoAccount.profile().nickname();
    }

    public record KakaoAccount(

            // 카카오 계정 이메일
            String email,

            // 카카오 프로필 정보
            Profile profile
    ) {
    }

    public record Profile(

            // 카카오 프로필 닉네임
            String nickname
    ) {
    }
}
