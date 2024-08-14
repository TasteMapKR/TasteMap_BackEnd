package tasteMap.backend.domain.feedback.dto.response;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
    private boolean status;
    private String content;
    private String name;
    private String profile_image;
}


