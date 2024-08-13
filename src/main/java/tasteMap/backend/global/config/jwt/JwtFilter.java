package tasteMap.backend.global.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tasteMap.backend.domain.member.entity.dto.MemberDTO;
import tasteMap.backend.global.config.security.CustomUserDetails;
import tasteMap.backend.global.response.ResponseDto;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access");

        // AccessToken이 없거나, 만료된 경우
        if (accessToken == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Access token is missing.");
            return;
        }
        if (jwtUtil.isExpired(accessToken)) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Access token is expired.");
            return;
        }

        // 카테고리가 Access인지 확인
        if (!"access".equals(jwtUtil.getCategory(accessToken))) {
            log.warn("Invalid token category");
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token category.");
            return;
        }

        // 유효한 JWT 토큰이 존재하는 경우
        String token = accessToken;

        // JWT 토큰에서 사용자 정보를 추출하여 MemberDTO를 생성
        MemberDTO memberDTO = MemberDTO.builder()
            .username(jwtUtil.getUsername(token))
            .role(jwtUtil.getRole(token))
            .build();

        // CustomOAuth2User 객체를 생성하여 사용자 정보를 설정
        CustomUserDetails customOAuth2User = new CustomUserDetails(memberDTO);

        // 사용자 정보를 기반으로 Spring Security의 Authentication 객체를 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(
            customOAuth2User,
            null,
            customOAuth2User.getAuthorities()
        );

        // SecurityContext에 Authentication을 설정하여 현재 사용자를 인증 상태로 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("User authenticated: {}", memberDTO.getUsername());

        // 필터 체인을 계속 진행하여 다음 필터나 요청 처리기로 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 인증 실패 시 상태 전달
     * @param response
     * @param status
     * @param message
     * @throws IOException
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 형식의 오류 응답 생성
        ResponseDto<String> responseDto = ResponseDto.fail(status, message);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(responseDto));
        response.getWriter().flush();
    }
}