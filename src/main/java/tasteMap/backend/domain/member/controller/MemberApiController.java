package tasteMap.backend.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tasteMap.backend.domain.member.entity.dto.MemberDTO;
import tasteMap.backend.global.config.security.CustomUserDetails;
import tasteMap.backend.global.response.ResponseDto;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApiController {
    // 로그인 사용자 조회
    @GetMapping("/")
    public ResponseEntity<?> getMember(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        MemberDTO member =customUserDetails.getMemberDTO();
        return ResponseEntity.status(200).body(ResponseDto.of("로그인 사용자 조회 성공", member));
    }

}
