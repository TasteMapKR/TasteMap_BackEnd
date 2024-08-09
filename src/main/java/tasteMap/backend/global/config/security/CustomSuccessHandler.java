package tasteMap.backend.global.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tasteMap.backend.global.config.jwt.JwtUtil;
import tasteMap.backend.global.config.jwt.refresh.service.RefreshService;
import tasteMap.backend.global.utils.CookieStore;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieStore cookieStore;
    private final RefreshService refreshService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String role = customUserDetails.getAuthorities().iterator().next().getAuthority();

        String AccessToken = jwtUtil.createAccessJwt(username, role, "access");
        String RefreshToken = jwtUtil.createRefreshJwt(username, role, "refresh");

        if(refreshService.existsByUsername(username)){
            refreshService.deleteByUsername(username);
        }

        refreshService.addRefresh(username,RefreshToken);

        response.setHeader("access", AccessToken);
        Cookie cookie = cookieStore.createCookie(RefreshToken);
        response.addCookie(cookie);

        response.sendRedirect("http://localhost:3000/");
    }
}