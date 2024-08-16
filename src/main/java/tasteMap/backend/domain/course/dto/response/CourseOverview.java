package tasteMap.backend.domain.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tasteMap.backend.domain.root.dto.RootDTO;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseOverview {
    private CourseDetailDTO course;
    private List<RootDTO> roots;
}
