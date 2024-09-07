package tasteMap.backend.domain.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.domain.feedback.dto.request.FeedbackDTO;
import tasteMap.backend.domain.feedback.dto.response.FeedbackApiDTO;
import tasteMap.backend.domain.feedback.dto.response.FeedbackCountsDto;
import tasteMap.backend.domain.feedback.dto.response.FeedbackResponseDTO;
import tasteMap.backend.domain.feedback.entity.Feedback;
import tasteMap.backend.domain.feedback.repository.FeedbackRepository;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.FeedbackErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public void save(FeedbackDTO feedbackDTO, Long id, String username){
        Member member = memberRepository.findByUsername(username);

        if(feedbackRepository.existsByRootIdAndMember(id, member)){
            throw new AppException(FeedbackErrorCode.ALREADY_EXISTS);
        }

        Feedback feedback = Feedback.builder()
            .content(feedbackDTO.getContent())
            .status(feedbackDTO.isStatus())
            .rootId(id)
            .member(member)
            .build();

        feedbackRepository.save(feedback);
    }
    @Transactional
    public void update(FeedbackDTO feedbackDTO, Long id, String username) {
        Member member = memberRepository.findByUsername(username);

        Feedback existingFeedback = feedbackRepository.findByRootIdAndMember(id, member)
            .orElseThrow(() -> new AppException(FeedbackErrorCode.NOT_FOUND));

        existingFeedback.setContent(feedbackDTO.getContent());
        existingFeedback.setStatus(feedbackDTO.isStatus());

        feedbackRepository.save(existingFeedback);
    }
    @Transactional
    public void delete(Long id, String username){
        Member member = memberRepository.findByUsername(username);
        feedbackRepository.deleteByIdAndMember(id, member);
    }
    public FeedbackApiDTO getAuFeedback(Long id, String username, Pageable pageable) {
        FeedbackCountsDto feedbackCounts = feedbackRepository.countFeedbacksByRootId(id);
        long positive = feedbackCounts.getPositiveCount();
        long negative = feedbackCounts.getNegativeCount();

        // 사용자를 찾고, 그에 대한 피드백과 페이지네이션된 피드백 목록을 가져옴
        Member member = memberRepository.findByUsername(username);

        FeedbackResponseDTO myFeedback = feedbackRepository.findMyFeedback(id, member);
        Page<FeedbackResponseDTO> feedbackResponseDTOPage = feedbackRepository.findFeedbackDTOByRootID(id, pageable);

        return new FeedbackApiDTO(positive, negative, myFeedback, feedbackResponseDTOPage);
    }
    public FeedbackApiDTO getFeedback(Long id, Pageable pageable) {
        FeedbackCountsDto feedbackCounts = feedbackRepository.countFeedbacksByRootId(id);
        long positive = feedbackCounts.getPositiveCount();
        long negative = feedbackCounts.getNegativeCount();


        Page<FeedbackResponseDTO> feedbackResponseDTOPage = feedbackRepository.findFeedbackDTOByRootID(id, pageable);

        return new FeedbackApiDTO(positive, negative, null, feedbackResponseDTOPage);
    }
}
