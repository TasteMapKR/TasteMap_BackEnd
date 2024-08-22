package tasteMap.backend.domain.course.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@Slf4j
public class CourseController {
    private final CourseService courseService;
    private final RootService rootService;
    private final S3UploaderImpl s3Uploader;

    /**
     * 먹거리 코스를 생성
     */
    @PostMapping
    public ResponseEntity<?> addCourse(
        @RequestPart("course") @Valid CourseDTO courseDTO,
        @RequestPart(value = "courseImage") MultipartFile courseImage,
        @RequestPart("roots") List<@Valid RootDTO> roots,
        @RequestPart(value = "rootImages") List<MultipartFile> rootImages,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Course course = courseService.save(courseDTO, customUserDetails.getUsername());
        s3Uploader.uploadCourse(courseImage, course.getId());

        rootService.save(roots, course);
        if (rootImages != null && !rootImages.isEmpty()) {
            log.info("{}", rootImages.size());
            s3Uploader.uploadRoot(rootImages, course.getId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of("코스 저장 성공", null));
    }


    /**
     * 먹거리 코스를 업데이트
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
                                          @RequestPart("course") @Valid CourseDTO courseDTO,
                                          @RequestPart(value = "courseImage", required = false) MultipartFile courseImage,
                                          @RequestPart("roots") List<@Valid RootDTO> roots,
                                          @RequestPart(value = "rootImages", required = false) List<MultipartFile> rootImages,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Course course = courseService.update(id, courseDTO, customUserDetails.getUsername());
        if(courseImage != null)
            s3Uploader.uploadCourse(courseImage, course.getId());
        rootService.updateRoots(course.getId(),roots);
        if(rootImages != null)
            s3Uploader.uploadRoot(rootImages, course.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of("코스 업데이트 성공",null));
    }



    /**
     * 먹거리 코스 제거
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails customUserDetails){

        courseService.delete(id, customUserDetails.getUsername());
        rootService.deleteRoots(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResponseDto.of("코스 삭제 성공",null));
    }
}