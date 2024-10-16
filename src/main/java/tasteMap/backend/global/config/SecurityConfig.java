package tasteMap.backend.global.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import tasteMap.backend.global.config.jwt.*;
import tasteMap.backend.global.config.security.CustomSuccessHandler;
import tasteMap.backend.global.oauth2.service.CustomOAuthUserService;

import java.util.Arrays;
import java.util.Collections;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler; // 인증 성공 시 처리할 커스텀 핸들러
    private final CustomOAuthUserService customOAuthUserService; // OAuth2 사용자 정보 서비스
    private final JwtAuthorizationFilter jwtAuthorizationFilter; // jwt 인증용 필터
    private final JwtExceptionFilter jwtExceptionFilter; // 필터 예외처리를 위한 필터
    private final WebCorsConfig webCorsConfig; // Cors 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(webCorsConfig.corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화
            .formLogin(AbstractHttpConfigurer::disable)  // Form 기반 로그인 비활성화
            .httpBasic(AbstractHttpConfigurer::disable)  // HTTP Basic 인증 비활성화
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가
            .addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class) // JwtExceptionFilter를 JwtAuthorizationFilter 전에 추가
            .exceptionHandling(handle -> handle.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuthUserService))
                .successHandler(customSuccessHandler)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/oauth2/code/**", "/refresh", "/api/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}