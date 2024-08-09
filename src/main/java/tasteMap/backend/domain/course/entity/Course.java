package tasteMap.backend.domain.course.entity;

import jakarta.persistence.*;
import lombok.*;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.root.entity.Root;

import java.util.ArrayList;
import java.util.List;

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
