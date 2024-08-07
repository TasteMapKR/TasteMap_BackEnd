package tasteMap.backend.global.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieStore {
    public Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(60 * 60); // 1시간
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
