package tasteMap.backend.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import tasteMap.backend.domain.member.entity.Enum.Role;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String profile_image;
}
