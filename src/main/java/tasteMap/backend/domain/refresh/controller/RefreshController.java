package tasteMap.backend.domain.refresh.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.config.jwt.JwtUtil;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.AuthErrorCode;
import tasteMap.backend.domain.refresh.service.RefreshService;
import tasteMap.backend.global.response.ResponseDto;
import tasteMap.backend.global.utils.CookieStore;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RefreshController {
    private final RefreshService refreshService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        log.info("리프레쉬 재발급");

        // 쿠키에서 Refresh 토큰 얻기
        String refresh = getRefreshTokenFromCookies(request);
        if (refresh == null) {
            throw new AppException(AuthErrorCode.REFRESH_TOKEN_NULL);
        }

        Map<String, String> responseBody = refreshService.refreshAccessToken(refresh, response);
        return ResponseEntity.ok(ResponseDto.of("Refresh 재발급 성공", responseBody));
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

