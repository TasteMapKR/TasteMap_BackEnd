package tasteMap.backend.domain.course.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tasteMap.backend.domain.course.dto.response.CourseMainPageDTO;
import tasteMap.backend.domain.course.dto.response.CourseMyDTO;
import tasteMap.backend.domain.course.dto.response.CourseDetailDTO;
import tasteMap.backend.domain.course.dto.response.CourseOverview;
import tasteMap.backend.domain.course.service.CourseApiService;

import tasteMap.backend.global.config.security.CustomUserDetails;
import tasteMap.backend.global.response.ResponseDto;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseApiController {
    private final CourseApiService courseApiService;

    @GetMapping("/my")
    public ResponseEntity<ResponseDto<Page<CourseMyDTO>>> myCourse(
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Page<CourseMyDTO> list = courseApiService.getCoursesByMemberId(customUserDetails.getUsername(), pageable);
        return ResponseEntity.status(200).body(ResponseDto.of("나의 코스 조회 성공", list));
    }
    @GetMapping("/category")
    public ResponseEntity<?> coursesByCategory(
        @RequestParam String category,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<CourseMainPageDTO> list = courseApiService.getCoursesByCategory(category, pageable);
        return ResponseEntity.status(200).body(ResponseDto.of("카테고리별 코스 조회 성공", list));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(
        @PathVariable Long id){
        CourseOverview result = courseApiService.getCourseById(id);
        return ResponseEntity.status(200).body(ResponseDto.of("코스 조회 성공", result));
    }
}
