package tasteMap.backend.domain.course;

import jakarta.persistence.*;
import lombok.*;
import tasteMap.backend.domain.member.entity.Member;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
