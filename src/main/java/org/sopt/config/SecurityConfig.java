package org.sopt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF 보호를 비활성화한다.
                .csrf(csrf -> csrf.disable())

                // 서버가 로그인 상태를 세션에 저장 X
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // 로그인과 토큰 재발급 API는 인증 없이 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/reissue").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/oauth2/kakao").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/oauth2/kakao/callback").permitAll()

                        // 로그아웃 API는 Access Token이 있는 사용자만 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").authenticated()

                        // 게시글 조회 API는 누구나 접근 가능
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/*").permitAll()

                        // 게시글 작성, 수정, 삭제 API는 인증이 필요
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/*").authenticated()

                        // 좋아요 추가, 취소 API는 인증이 필요
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts/*/likes").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/*/likes").authenticated()

                        // Swagger 문서는 인증 없이 접근 가능
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 그 외 요청 우선 허용
                        .anyRequest().permitAll()
                )

                // UsernamePasswordAuthenticationFilter 전에 JWT 필터 실행
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 해시 알고리즘 사용
        return new BCryptPasswordEncoder();
    }
}
