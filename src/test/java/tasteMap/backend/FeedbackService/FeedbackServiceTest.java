package tasteMap.backend.FeedbackService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import tasteMap.backend.domain.feedback.dto.request.FeedbackDTO;
import tasteMap.backend.domain.feedback.dto.response.FeedbackApiDTO;
import tasteMap.backend.domain.feedback.dto.response.FeedbackCountsDto;
import tasteMap.backend.domain.feedback.dto.response.FeedbackResponseDTO;
import tasteMap.backend.domain.feedback.entity.Feedback;
import tasteMap.backend.domain.feedback.repository.FeedbackRepository;
import tasteMap.backend.domain.feedback.service.FeedbackService;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.exception.AppException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveFeedback_Success() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(true,"content");
        Long rootId = 1L;
        String username = "user1";

        Member member = new Member(username);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(feedbackRepository.existsByRootIdAndMember(rootId, member)).thenReturn(false);

        feedbackService.save(feedbackDTO, rootId, username);

        verify(feedbackRepository).save(any(Feedback.class));
    }

    @Test
    public void testSaveFeedback_AlreadyExists() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(true,"content");
        Long rootId = 1L;
        String username = "user1";

        Member member = new Member(username);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(feedbackRepository.existsByRootIdAndMember(rootId, member)).thenReturn(true);

        assertThrows(AppException.class, () -> feedbackService.save(feedbackDTO, rootId, username));
    }

    @Test
    public void testUpdateFeedback_Success() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(false,"new content");
        Long rootId = 1L;
        String username = "user1";

        Member member = new Member(username);
        Feedback existingFeedback = Feedback.builder()
            .rootId(rootId)
            .content("old content")
            .member(member)
            .status(true)
            .build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(feedbackRepository.findByRootIdAndMember(rootId, member)).thenReturn(Optional.of(existingFeedback));

        feedbackService.update(feedbackDTO, rootId, username);

        verify(feedbackRepository).save(existingFeedback);
        assertEquals("new content", existingFeedback.getContent());
        assertFalse(existingFeedback.isStatus());
    }

    @Test
    public void testUpdateFeedback_NotFound() {
        FeedbackDTO feedbackDTO = new FeedbackDTO( true,"content");
        Long rootId = 1L;
        String username = "user1";

        Member member = new Member(username);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(feedbackRepository.findByRootIdAndMember(rootId, member)).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> feedbackService.update(feedbackDTO, rootId, username));
    }

    @Test
    public void testDeleteFeedback_Success() {
        Long rootId = 1L;
        String username = "user1";
        Member member = new Member(username);

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        doNothing().when(feedbackRepository).deleteByIdAndMember(rootId, member);

        feedbackService.delete(rootId, username);

        verify(feedbackRepository).deleteByIdAndMember(rootId, member);
    }
    @Test
    public void testGetFeedback() {
        Long rootId = 1L;
        String username = "user1";
        Pageable pageable = Pageable.unpaged();

        Member member = new Member(username);
        FeedbackCountsDto feedbackCounts = new FeedbackCountsDto(10, 5);
        FeedbackResponseDTO myFeedback = new FeedbackResponseDTO(true, "my feedback", "name", "profile_image");
        Page<FeedbackResponseDTO> feedbackPage = new PageImpl<>(List.of(myFeedback));

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(feedbackRepository.countFeedbacksByRootId(rootId)).thenReturn(feedbackCounts);
        when(feedbackRepository.findMyFeedback(rootId, member)).thenReturn(myFeedback);
        when(feedbackRepository.findFeedbackDTOByRootID(rootId, pageable)).thenReturn(feedbackPage);

        FeedbackApiDTO result = feedbackService.getAuFeedback(rootId, username, pageable);

        assertEquals(10, result.getPositive());
        assertEquals(5, result.getNegative());
        assertEquals(myFeedback, result.getMyFeedbackResponseDTO());
        assertEquals(feedbackPage, result.getFeedbackResponseDTOs());
    }
}