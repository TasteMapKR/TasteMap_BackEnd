package tasteMap.backend.entity.root;

import jakarta.persistence.*;
import lombok.*;
import tasteMap.backend.entity.course.Course;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Root {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
