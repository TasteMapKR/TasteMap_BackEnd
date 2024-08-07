package tasteMap.backend.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tasteMap.backend.domain.member.entity.dto.MemberDTO;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        MemberDTO memberDTO = MemberDTO.builder()
            .username(member.getUsername())
            .email(member.getEmail())
            .name(member.getName())
            .build();
        return new CustomUserDetails(memberDTO);
    }
}