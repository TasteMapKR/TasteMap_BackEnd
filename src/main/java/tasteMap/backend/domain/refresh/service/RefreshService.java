package tasteMap.backend.domain.refresh.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.domain.refresh.entity.Refresh;
import tasteMap.backend.domain.refresh.repository.RefreshRepository;
import tasteMap.backend.global.config.jwt.JwtUtil;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.AuthErrorCode;
import tasteMap.backend.global.exception.errorCode.MemberErrorCode;
import tasteMap.backend.global.utils.CookieStore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshService {
    private final RefreshRepository refreshRepository;
    private final long RefreshTokenExpireTime = 1000L * 60 * 60 * 24; // 1일
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final CookieStore cookieStore;

    @Transactional
    public void addRefresh(String username, String token) {
        Refresh refresh = Refresh.builder()
            .username(username)
            .token(token)
            .expiration(RefreshTokenExpireTime) // Redis의 TTL(Time-to-live) 기능 사용
            .build();
        refreshRepository.save(refresh);
    }

    @Transactional(readOnly = true)
    public Boolean existsByToken(String token) {
        return refreshRepository.existsByToken(token);
    }

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return refreshRepository.existsByUsername(username);
    }

    @Transactional
    public void deleteByToken(String token) {
        refreshRepository.deleteByToken(token);
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshRepository.deleteByUsername(username);
    }

    public List<Refresh> get(){return refreshRepository.findAll();}

    public Map<String, String> refreshAccessToken(String refreshToken, HttpServletResponse response) {
        // 만료 시간 체크 및 토큰 검증
        if (jwtUtil.isExpired(refreshToken)) {
            throw new AppException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인
        if (!"refresh".equals(jwtUtil.getCategory(refreshToken))) {
            throw new AppException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // DB에 저장되어 있는지 확인
        String username = jwtUtil.getUsername(refreshToken);
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 새로운 Access 및 Refresh 토큰 생성
        String newAccess = jwtUtil.createAccessJwt(member.getUsername(), String.valueOf(member.getRole()), "access");
        String newRefresh = jwtUtil.createRefreshJwt(member.getUsername(), String.valueOf(member.getRole()), "refresh");

        // Refresh 토큰 저장 및 응답 설정
        response.addCookie(cookieStore.createCookie(newRefresh));
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("name", member.getName());
        responseBody.put("profile", member.getProfile_image());
        response.setHeader("access", newAccess);

        return responseBody;
    }
}

