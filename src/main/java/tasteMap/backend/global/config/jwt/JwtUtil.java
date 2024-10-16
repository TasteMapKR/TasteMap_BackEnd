package tasteMap.backend.global.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
@Slf4j
public class JwtUtil {
    private final SecretKey secretKey;
    private final long RefreshTokenRemiteTime = 1000L * 60 * 60 * 24; // 1일
    private final long AccessTokenRemiteTime = 60 * 60 * 1000L; // 1시간

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 시크릿 키 생성
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser() // parserBuilder() 대신 parser() 사용
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid");
            throw new JwtException("유효하지 않은 토큰");
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails");
            throw new JwtException("시그니처가 유효하지 않은 토큰");
        } catch (ExpiredJwtException exception) {
            log.error("JWT expired");
            throw new JwtException("만료된 토큰");
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace");
            throw new JwtException("값이 들어있지 않은 토큰");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }
        return false;
    }

    // 유저네임 가져오기
    public String getUsername(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("username", String.class);
    }

    // 권한 가져오기
    public String getRole(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    }

    // 토큰 만료 여부 확인
    public Boolean isExpired(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .before(new Date());
    }

    // 카테고리 가져오기
    public String getCategory(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("category", String.class);
    }


    // Access JWT 생성
    public String createAccessJwt(String username, String role, String category) {
        return Jwts.builder()
            .claim("category", category)
            .claim("username", username)
            .claim("role", role)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + AccessTokenRemiteTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    // Refresh JWT 생성
    public String createRefreshJwt(String username, String role, String category) {
        return Jwts.builder()
            .claim("category", category)
            .claim("username", username)
            .claim("role", role)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + RefreshTokenRemiteTime))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }
}
