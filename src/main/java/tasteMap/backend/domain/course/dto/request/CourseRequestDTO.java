package tasteMap.backend.domain.course.dto.request;

import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import tasteMap.backend.domain.root.dto.RootDTO;

import java.util.List;
@Builder
public record CourseRequestDTO(   @Valid
                                   CourseDTO courseDTO,
                                   MultipartFile courseImage,
                                   @Valid
                                   List<RootDTO> roots,
                                   List<MultipartFile> rootImages) {
}