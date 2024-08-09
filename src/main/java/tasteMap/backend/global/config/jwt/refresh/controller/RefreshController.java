package tasteMap.backend.global.config.jwt.refresh.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.config.jwt.JwtUtil;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.AuthErrorCode;
import tasteMap.backend.global.config.jwt.refresh.service.RefreshService;
import tasteMap.backend.global.utils.CookieStore;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RefreshController {
    private final JwtUtil jwtUtil;
    private final CookieStore cookieStore;
    private final RefreshService refreshService;
    private final MemberRepository memberRepository;
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

        //쿠키에서 Refresh 토큰 얻기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new AppException(AuthErrorCode.REFRESH_TOKEN_NULL);
        }

        //만료 시간 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new AppException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            throw new AppException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshService.existsByRefresh(refresh);
        if (!isExist) {

            throw new AppException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtUtil.getUsername(refresh);
        Member member = memberRepository.findByUsername(username);

        String newAccess= jwtUtil.createAccessJwt(member.getUsername(), String.valueOf(member.getRole()), "access");
        String newRefresh = jwtUtil.createRefreshJwt(member.getUsername(), String.valueOf(member.getRole()), "refresh");

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshService.deleteByRefresh(refresh);
        refreshService.addRefresh(username, newRefresh);

        //response
        response.setHeader("access", newAccess);
        log.info("{}",newAccess);
        response.addCookie(cookieStore.createCookie(newRefresh));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}