package tasteMap.backend.domain.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDetailDTO {
    private String title;
    private String category;
    private String content;
    private String name;
    private String profile_image;
}
