package tasteMap.backend.domain.course.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tasteMap.backend.domain.root.dto.RootDTO;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    @NotEmpty(message = " 코스 제목은 필수 입력 값입니다.")
    private String title;
    @NotEmpty(message = " 코스 내용은 필수 입력 값입니다.")
    private String content;
    private List<RootDTO> roots;

}