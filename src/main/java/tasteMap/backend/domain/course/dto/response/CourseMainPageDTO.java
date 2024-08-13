package tasteMap.backend.domain.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseMainPageDTO {
    private Long id;
    private String title;
    private String category;
    private String name;
    private String profile_image;
}
