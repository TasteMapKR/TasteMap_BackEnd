package tasteMap.backend.global.config.jwt;

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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tasteMap.backend.global.config.security.CustomUserDetails;
import tasteMap.backend.global.config.security.UserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private static final String AUTHORIZATION_HEADER = "access";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);
        if (StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
            String username = jwtUtil.getUsername(accessToken);
            CustomUserDetails customUserDetails = userDetailsService.loadUserByUsername(username);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("User authenticated: {}", jwtUtil.getUsername(accessToken));
        }

        filterChain.doFilter(request, response);  // 필터 체인 계속 진행
    }

    private String resolveToken(HttpServletRequest request) {
        // "access" 헤더에서 토큰 추출
        return request.getHeader(AUTHORIZATION_HEADER);
    }


}


