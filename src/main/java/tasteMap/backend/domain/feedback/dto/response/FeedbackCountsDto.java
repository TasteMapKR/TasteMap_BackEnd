package tasteMap.backend.domain.feedback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbackCountsDto {
    private long positiveCount;
    private long negativeCount;
}