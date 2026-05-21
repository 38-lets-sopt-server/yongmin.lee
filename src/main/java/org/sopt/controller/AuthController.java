package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.common.response.BaseResponse;
import org.sopt.common.response.SuccessCode;
import org.sopt.dto.auth.request.LoginRequest;
import org.sopt.dto.auth.request.ReissueTokenRequest;
import org.sopt.dto.auth.response.LoginResponse;
import org.sopt.dto.auth.response.ReissueTokenResponse;
import org.sopt.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 access token, refresh token을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(
            @RequestBody LoginRequest request
            ){
        // 로그인 요청을 서비스로 전달하고 토큰 응답을 받음
        LoginResponse response = authService.login(request);

        return ResponseEntity
                .status(SuccessCode.LOGIN_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.LOGIN_SUCCESS, response));
    }

    @Operation(summary = "토큰 재발급", description = "refresh token으로 새로운 access token과 refresh token을 발급받습니다.")
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<ReissueTokenResponse>> reissue(
            @RequestBody ReissueTokenRequest request
            ){
        // refresh token 검증 후 access token과 refresh token을 재 발급
        ReissueTokenResponse response = authService.reissue(request);

        return ResponseEntity
                .status(SuccessCode.TOKEN_REISSUE_SUCCESS.getStatus())
                .body(BaseResponse.success(SuccessCode.TOKEN_REISSUE_SUCCESS, response));
    }


}
