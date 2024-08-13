package tasteMap.backend.global.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import tasteMap.backend.global.config.security.CustomSuccessHandler;
import tasteMap.backend.global.config.jwt.JwtFilter;
import tasteMap.backend.global.config.jwt.JwtUtil;
import tasteMap.backend.global.oauth2.service.CustomOAuthUserService;

import java.util.Arrays;
import java.util.Collections;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil; // JWT 유틸리티 클래스
    private final CustomSuccessHandler customSuccessHandler; // 인증 성공 시 처리할 커스텀 핸들러
    private final CustomOAuthUserService customOAuthUserService; // OAuth2 사용자 정보 서비스
    private final JwtFilter jwtFilter; // JWT 필터

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS 설정 시작
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration configuration = new CorsConfiguration();

                    // 허용할 출처(Origin)를 설정합니다.
                    // 여기에 설정된 출처만 요청을 허용합니다.
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));

                    // 허용할 HTTP 메서드를 설정합니다.
                    // 여기서 '*'는 모든 메서드를 허용한다는 의미입니다.
                    configuration.setAllowedMethods(Collections.singletonList("*"));

                    // 자격 증명(Credentials)을 포함한 요청을 허용합니다.
                    // 여기서 `true`로 설정하면 쿠키와 인증 헤더 등을 허용합니다.
                    configuration.setAllowCredentials(true);

                    // 허용할 요청 헤더를 설정합니다.
                    // 여기서 '*'는 모든 헤더를 허용한다는 의미입니다.
                    configuration.setAllowedHeaders(Collections.singletonList("*"));

                    // CORS 요청의 유효 기간을 설정합니다.
                    // 이 시간 동안 CORS 요청을 캐시할 수 있습니다. (초 단위)
                    configuration.setMaxAge(3600L);

                    // 클라이언트에게 노출할 헤더를 설정합니다.
                    // 클라이언트가 이 헤더를 읽을 수 있도록 허용합니다.
                    configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "access"));

                    return configuration;
                }
            }));

        // CSRF 보호 비활성화
        // CSRF 보호를 비활성화하여 웹 애플리케이션의 보안 조치를 단순화합니다.
        http
            .csrf(auth -> auth.disable());

        // Form 기반 로그인 비활성화
        // 기본 제공되는 Form 로그인 기능을 비활성화하여 커스텀 로그인 로직을 사용할 수 있습니다.
        http
            .formLogin(auth -> auth.disable());

        // HTTP Basic 인증 비활성화
        // HTTP Basic 인증을 비활성화하여, JWT와 같은 다른 인증 방식을 사용할 수 있습니다.
        http
            .httpBasic(auth -> auth.disable());

        // JWT 필터 추가
        // `JwtFilter`를 필터 체인에 추가하여 JWT 토큰 기반 인증을 처리합니다.
        // `OAuth2LoginAuthenticationFilter` 이후에 위치하여 OAuth2 인증을 먼저 처리한 후 JWT 검증을 수행합니다.
        http
            .addFilterAfter(jwtFilter, OAuth2LoginAuthenticationFilter.class);

        // OAuth2 로그인 설정
        // OAuth2 인증을 설정합니다. 커스텀 사용자 서비스와 성공 핸들러를 지정합니다.
        http
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuthUserService))
                .successHandler(customSuccessHandler) // 커스텀 성공 핸들러 설정
            );

        // 요청 권한 설정
        // 특정 경로에 대한 권한을 설정합니다. 루트 경로는 모든 사용자에게 허용하고,
        // 나머지 모든 요청은 인증된 사용자만 접근할 수 있도록 설정합니다.
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/**", "/refresh", "/**").permitAll() // 로그인 및 리프레시 경로를 인증 없이 접근 허용
                .anyRequest().authenticated()); // 나머지 모든 요청은 인증된 사용자만 접근 허용

        // 세션 관리 설정: STATELESS
        // 세션을 사용하지 않도록 설정하여, JWT와 같은 토큰 기반 인증만 사용하도록 합니다.
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}