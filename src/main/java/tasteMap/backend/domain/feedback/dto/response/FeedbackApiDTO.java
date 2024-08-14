package tasteMap.backend.domain.feedback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackApiDTO {
    private Long positive;
    private Long negative;
    private FeedbackResponseDTO myFeedbackResponseDTO;
    private Page<FeedbackResponseDTO> feedbackResponseDTOs;
}
