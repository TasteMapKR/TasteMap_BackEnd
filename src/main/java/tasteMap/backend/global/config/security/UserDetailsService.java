package tasteMap.backend.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tasteMap.backend.domain.member.entity.dto.MemberDTO;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.config.security.CustomUserDetails;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.MemberErrorCode;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(MemberErrorCode.MEMBER_NOT_FOUND));
        MemberDTO memberDTO = MemberDTO.builder()
            .username(member.getUsername())
            .email(member.getEmail())
            .name(member.getName())
            .build();
        return new CustomUserDetails(memberDTO);
    }
}