package org.sopt.service;

import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.ErrorCode;
import org.sopt.domain.RefreshToken;
import org.sopt.domain.User;
import org.sopt.dto.auth.request.LoginRequest;
import org.sopt.dto.auth.request.ReissueTokenRequest;
import org.sopt.dto.auth.response.LoginResponse;
import org.sopt.dto.auth.response.ReissueTokenResponse;
import org.sopt.repository.RefreshTokenRepository;
import org.sopt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인
    @Transactional
    public LoginResponse login(LoginRequest request){

        // 이메일로 로그인할 유저 조회
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 요청으로 받은 비밀번호가 맞는지 확인
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 로그인에 성공하면 access token과 refresh token을 새로 발급
        TokenPair tokenPair = generateTokenPair(user);

        // 기존 refresh token 삭제
        refreshTokenRepository.deleteByUserId(user.getId());

        // 새 refresh token을 DB에 저장
        RefreshToken savedRefreshToken = RefreshToken.of(
                user.getId(),
                tokenPair.refreshToken(),
                jwtService.getRefreshTokenExpiresInSeconds()
        );

        refreshTokenRepository.save(savedRefreshToken);

        return new LoginResponse(tokenPair.accessToken(), tokenPair.refreshToken());
    }

    // accessToken 재발급
    @Transactional
    public ReissueTokenResponse reissue(ReissueTokenRequest request){

        Long userId;

        try{
            // refresh token 검증 및 user id 추출
            userId = jwtService.verifyAndGetUserId(request.refreshToken());
        }
        catch (Exception e){
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // DB에 저장된 refresh token인지 확인
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // refresh token의 만료 시간이 지났는지 확인
        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())){
            refreshTokenRepository.deleteByUserId(userId);
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        // 토큰에 해당하는 유저 조회
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 새로운 access token과 refresh token을 발급
        TokenPair tokenPair = generateTokenPair(user);

        // DB에 저장된 refresh token 값을 새 토큰으로 교체
        refreshToken.rotate(
                tokenPair.refreshToken(),
                jwtService.getRefreshTokenExpiresInSeconds()
        );

        return new ReissueTokenResponse(tokenPair.accessToken(), tokenPair.refreshToken());
    }

    // 사용자 정보를 기반으로 Access Token과 Refresh Token을 함께 발급
    private TokenPair generateTokenPair(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        return new TokenPair(accessToken, refreshToken);
    }

    // Access Token과 Refresh Token을 묶어서 전달
    private record TokenPair(
            String accessToken,
            String refreshToken
    ){
    }
}

