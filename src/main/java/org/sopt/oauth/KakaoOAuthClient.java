package org.sopt.oauth;

import org.sopt.dto.oauth.KakaoTokenResponse;
import org.sopt.dto.oauth.KakaoUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOAuthClient {

    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String BEARER_PREFIX = "Bearer ";

    private final RestClient restClient;
    private final String restApiKey;
    private final String clientSecret;
    private final String redirectUri;

    public KakaoOAuthClient(
            @Value("${oauth.kakao.rest-api-key}") String restApiKey,
            @Value("${oauth.kakao.client-secret:}") String clientSecret,
            @Value("${oauth.kakao.redirect-uri}") String redirectUri
    ) {
        this.restClient = RestClient.create();
        this.restApiKey = restApiKey;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public KakaoTokenResponse requestToken(String code) {
        // 카카오 인가 코드로 카카오 Access Token 요청
        return restClient.post()
                .uri(KAKAO_TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(buildTokenRequestBody(code))
                .retrieve()
                .body(KakaoTokenResponse.class);
    }

    public KakaoUserResponse requestUserInfo(String kakaoAccessToken) {
        // 카카오 Access Token으로 카카오 사용자 정보 요청
        return restClient.get()
                .uri(KAKAO_USER_INFO_URI)
                .header("Authorization", BEARER_PREFIX + kakaoAccessToken)
                .retrieve()
                .body(KakaoUserResponse.class);
    }

    private MultiValueMap<String, String> buildTokenRequestBody(String code) {
        // 카카오 토큰 API가 요구하는 form-urlencoded 요청 본문 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        if (clientSecret != null && !clientSecret.isBlank()) {
            body.add("client_secret", clientSecret);
        }

        return body;
    }
}
