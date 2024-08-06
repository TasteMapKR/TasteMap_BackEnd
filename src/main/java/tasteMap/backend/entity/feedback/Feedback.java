package tasteMap.backend.entity.feedback;

import jakarta.persistence.*;
import lombok.*;
import tasteMap.backend.entity.member.Member;
import tasteMap.backend.entity.root.Root;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean status;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "root_id") // 엔티티 대신 ID만 저장
    private Long rootId;
}