package tasteMap.backend.global.jwt;

import jakarta.persistence.*;
import lombok.*;
import tasteMap.backend.entity.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String refresh;

    private LocalDateTime expiration;
}