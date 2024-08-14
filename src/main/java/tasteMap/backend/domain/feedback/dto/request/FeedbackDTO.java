package tasteMap.backend.domain.feedback.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tasteMap.backend.domain.member.entity.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private boolean status;
    @NotEmpty(message = "평가 내용은 필수 입력 값입니다.")
    private String content;
}
