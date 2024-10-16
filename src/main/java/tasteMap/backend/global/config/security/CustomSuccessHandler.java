package tasteMap.backend.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tasteMap.backend.global.config.jwt.JwtUtil;
import tasteMap.backend.domain.refresh.service.RefreshService;
import tasteMap.backend.global.utils.CookieStore;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieStore cookieStore;
    private final RefreshService refreshService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String role = customUserDetails.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createAccessJwt(username, role, "access");
        String refreshToken = jwtUtil.createRefreshJwt(username, role, "refresh");

        if(refreshService.existsByUsername(username)){
            refreshService.deleteByUsername(username);
        }

        refreshService.addRefresh(username, refreshToken);
        log.info("AccessToken = {}", accessToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);

        Cookie cookie = cookieStore.createCookie(refreshToken);
        response.addCookie(cookie);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(tokenMap);

        response.getWriter().write(jsonResponse);
    }
}