package tasteMap.backend.global.config.jwt.refresh.entity;

import jakarta.persistence.*;
import lombok.*;
import tasteMap.backend.domain.member.entity.Member;

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

    private String username;

    private String refresh;

    private String expiration;
}