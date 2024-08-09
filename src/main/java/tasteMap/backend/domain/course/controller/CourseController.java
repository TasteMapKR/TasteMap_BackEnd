package tasteMap.backend.domain.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tasteMap.backend.domain.course.dto.CourseDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.service.CourseService;
import tasteMap.backend.domain.root.dto.RootDTO;
import tasteMap.backend.domain.root.service.RootService;
import tasteMap.backend.global.config.security.CustomSuccessHandler;
import tasteMap.backend.global.config.security.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final RootService rootService;

    @PostMapping()
    public ResponseEntity<String> addCourse(
        @RequestPart("course") @Valid CourseDTO courseDTO,
        @RequestPart("courseImage") MultipartFile courseImage,
        @RequestPart("roots") List<@Valid RootDTO> roots,
        @RequestPart("rootImages") List<MultipartFile> rootImages,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Course course = courseService.save(courseDTO,customUserDetails.getUsername());
        //id를 이용해서 img 저장

        rootService.save(roots, course);
        //순서를 이용해서 img 저장
        return new ResponseEntity<>("Course added successfully", HttpStatus.OK);
    }
}