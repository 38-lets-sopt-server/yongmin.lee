package org.sopt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sopt.auth.BlacklistTokenStore;
import org.sopt.common.exception.ErrorCode;
import org.sopt.common.response.BaseResponse;
import org.sopt.domain.User;
import org.sopt.repository.UserRepository;
import org.sopt.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final ObjectMapper objectMapper;
    private final BlacklistTokenStore blacklistTokenStore;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository, ObjectMapper objectMapper, BlacklistTokenStore blacklistTokenStore) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.blacklistTokenStore = blacklistTokenStore;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Authorization 헤더가 없거나 Bearer 토큰 형식이 아니면 인증 처리 없이 다음 필터로 넘김
        if (!hasBearerToken(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            // 로그아웃 처리된 Access Token이면 인증을 중단
            if (blacklistTokenStore.exists(accessToken)){
                throw new IllegalArgumentException("로그아웃된 토큰입니다.");
            }

            // access token 검증 및 userId 추출
            Long userId = jwtService.verifyAndGetUserId(accessToken);

            // userId 검증
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // Spring Security가 사용할 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.getId(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            // 요청 정보를 인증 객체에 함께 담음
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // 현재 요청의 인증 정보를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 토큰이 유효하지 않으면 인증 정보를 비우고 401 응답을 반환
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            // 공통 에러 응답 형식에 맞춰서 JSON으로 변환
            BaseResponse<Void> errorResponse = BaseResponse.error(ErrorCode.INVALID_TOKEN);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }

    private boolean hasBearerToken(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX);
    }
}
