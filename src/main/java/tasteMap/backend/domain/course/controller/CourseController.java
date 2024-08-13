package tasteMap.backend.domain.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tasteMap.backend.domain.course.dto.request.CourseDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.service.CourseService;
import tasteMap.backend.domain.root.dto.RootDTO;
import tasteMap.backend.domain.root.service.RootService;
import tasteMap.backend.global.S3.service.S3UploaderImpl;
import tasteMap.backend.global.config.security.CustomUserDetails;
import tasteMap.backend.global.response.ResponseDto;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final RootService rootService;
    private final S3UploaderImpl s3Uploader;

    /**
     * 먹거리 코스를 생성
     */
    @PostMapping()
    public ResponseEntity<?> addCourse(
        @RequestPart("course") @Valid CourseDTO courseDTO,
        @RequestPart("courseImage") MultipartFile courseImage,
        @RequestPart("roots") List<@Valid RootDTO> roots,
        @RequestPart("rootImages") List<MultipartFile> rootImages,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Course course = courseService.save(courseDTO,customUserDetails.getUsername());
        s3Uploader.uploadCourse(courseImage, course.getId());

        rootService.save(roots, course);
        s3Uploader.uploadRoot(rootImages, course.getId());

        return ResponseEntity.status(200).body(ResponseDto.of("코스 저장 성공",null));
    }

    /**
     * 먹거리 코스를 업데이트
     */
    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
                                          @RequestPart("course") @Valid CourseDTO courseDTO,
                                          @RequestPart("courseImage") MultipartFile courseImage,
                                          @RequestPart("roots") List<@Valid RootDTO> roots,
                                          @RequestPart("rootImages") List<MultipartFile> rootImages,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Course course = courseService.update(id, courseDTO, customUserDetails.getUsername());
        s3Uploader.uploadCourse(courseImage, course.getId());

        rootService.updateRoots(course.getId(),roots);
        s3Uploader.uploadRoot(rootImages, course.getId());

        return ResponseEntity.status(200).body(ResponseDto.of("코스 업데이트 성공",null));
    }

    /**
     * 먹거리 코스 제거
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteCourse(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails customUserDetails){

        courseService.delete(id, customUserDetails.getUsername());
        rootService.deleteRoots(id);

        return ResponseEntity.status(200).body(ResponseDto.of("코스 삭제 성공",null));
    }
}