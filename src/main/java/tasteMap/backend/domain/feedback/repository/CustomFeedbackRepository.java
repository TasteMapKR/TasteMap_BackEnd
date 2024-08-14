package tasteMap.backend.domain.feedback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tasteMap.backend.domain.course.dto.response.CourseMyDTO;
import tasteMap.backend.domain.feedback.dto.response.FeedbackCountsDto;
import tasteMap.backend.domain.feedback.dto.response.FeedbackResponseDTO;
import tasteMap.backend.domain.member.entity.Member;

public interface CustomFeedbackRepository {
    Page<FeedbackResponseDTO> findFeedbackDTOByRootID(Long rootId, Pageable pageable);
    FeedbackResponseDTO findMyFeedback(Long rootId, Member member);
    FeedbackCountsDto countFeedbacksByRootId(Long rootId);
}
