package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.response.BaseResponse;
import org.sopt.common.response.SuccessCode;
import org.sopt.dto.auth.response.LoginResponse;
import org.sopt.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "소셜 로그인 관련 API")
@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuthController {

    private static final String KAKAO_AUTHORIZE_URI = "https://kauth.kakao.com/oauth/authorize";
    private static final String RESPONSE_TYPE_CODE = "code";

    private final AuthService authService;
    private final String kakaoRestApiKey;
    private final String kakaoRedirectUri;

    public OAuthController(
            AuthService authService,
            @Value("${oauth.kakao.rest-api-key}") String kakaoRestApiKey,
            @Value("${oauth.kakao.redirect-uri}") String kakaoRedirectUri
    ) {
        this.authService = authService;
        this.kakaoRestApiKey = kakaoRestApiKey;
        this.kakaoRedirectUri = kakaoRedirectUri;
    }

    @Operation(summary = "카카오 로그인 페이지 이동", description = "카카오 인가 URL로 리다이렉트합니다.")
    @GetMapping("/kakao")
    public ResponseEntity<Void> redirectToKakaoLogin() {
        // 카카오 로그인 화면으로 이동
        String kakaoLoginUrl = KAKAO_AUTHORIZE_URI
                + "?response_type=" + RESPONSE_TYPE_CODE
                + "&client_id=" + kakaoRestApiKey
                + "&redirect_uri=" + kakaoRedirectUri;

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, kakaoLoginUrl)
                .build();
    }

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 인가 코드를 받아 우리 서버 JWT를 발급합니다.")
    @GetMapping("/kakao/callback")
    public ResponseEntity<BaseResponse<LoginResponse>> kakaoLogin(
            @RequestParam("code") String code
    ) {
        // 카카오 인가 코드로 카카오 유저 정보를 조회하고 우리 서버 JWT 발급
        LoginResponse response = authService.kakaoLogin(code);

        return ResponseEntity
                .status(SuccessCode.LOGIN_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.LOGIN_SUCCESS, response));
    }
}
