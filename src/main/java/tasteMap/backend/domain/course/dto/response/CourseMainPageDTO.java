package tasteMap.backend.domain.course.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseMainPageDTO {
    private Long id;
    private String title;
    private String category;
    private String name;
    private String profile_image;
}
