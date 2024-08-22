package tasteMap.backend.domain.feedback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tasteMap.backend.domain.course.dto.request.CourseDTO;
import tasteMap.backend.domain.feedback.dto.request.FeedbackDTO;
import tasteMap.backend.domain.feedback.dto.response.FeedbackApiDTO;
import tasteMap.backend.domain.feedback.service.FeedbackService;
import tasteMap.backend.global.config.security.CustomUserDetails;
import tasteMap.backend.global.response.ResponseDto;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/feedback/{id}")
    public ResponseEntity<?> addFeedback(@PathVariable Long id,
                                         @RequestBody @Valid FeedbackDTO feedbackDTO,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails
                                         ){
        feedbackService.save(feedbackDTO,id, customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of("평가 생성 성공", feedbackDTO));
    }

    @PutMapping("/feedback/{id}")
    public ResponseEntity<?> updateFeedback(@PathVariable Long id,
                                            @RequestBody @Valid FeedbackDTO feedbackDTO,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        feedbackService.update(feedbackDTO,id, customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of("평가 수정 성공", feedbackDTO));
    }

    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        feedbackService.delete(id, customUserDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResponseDto.of("평가 제거 성공", null));
    }
    @GetMapping("/api/feedback/{id}")
    public ResponseEntity<?> getFeedback(@PathVariable Long id,
                                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        FeedbackApiDTO feedbackApiDTO;
        if(customUserDetails == null){
            feedbackApiDTO = feedbackService.getFeedback(id, pageable);
        }
        else {
            feedbackApiDTO = feedbackService.getAuFeedback(id, customUserDetails.getUsername(), pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of("평가 조회 성공", feedbackApiDTO));
    }
}
