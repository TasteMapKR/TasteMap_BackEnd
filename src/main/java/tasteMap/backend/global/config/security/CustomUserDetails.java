package tasteMap.backend.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import tasteMap.backend.domain.member.entity.dto.MemberDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
@RequiredArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails {
    private final MemberDTO memberDTO;

    @Override
    public String getPassword() {
        return null; // OAuth2 사용자에게는 비밀번호가 없으므로 null 반환
    }

    @Override
    public String getUsername() {
        return memberDTO.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap(); // OAuth2User가 기본적으로 제공하는 속성을 사용하는 경우, 빈 맵을 반환
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(memberDTO.getRole()));
    }

    public String getEmail() {
        return memberDTO.getEmail();
    }

    @Override
    public String getName() {
        return memberDTO.getName();
    }
}
