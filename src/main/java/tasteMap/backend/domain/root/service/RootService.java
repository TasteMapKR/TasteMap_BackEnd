package tasteMap.backend.domain.root.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.repository.CourseRepository;
import tasteMap.backend.domain.root.dto.RootDTO;
import tasteMap.backend.domain.root.entity.Root;
import tasteMap.backend.domain.root.repository.RootRepository;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.CourseErrorCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RootService {
    private final RootRepository rootRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void save(List<RootDTO> roots, Course course) {
        for (RootDTO rootDto : roots) {
            Root root = Root.builder()
                .title(rootDto.getTitle())
                .content(rootDto.getContent())
                .address(rootDto.getAddress())
                .course(course)
                .build();
            rootRepository.save(root);
        }
    }

    @Transactional
    public void updateRoots(Long courseId, List<RootDTO> rootDTOs) {
        // Course 조회
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // 기존 Root 객체 모두 삭제
        rootRepository.deleteByCourse(course);

        // 새 Root 객체 추가
        for (RootDTO rootDTO : rootDTOs) {
            Root root = Root.builder()
                .title(rootDTO.getTitle())
                .content(rootDTO.getContent())
                .address(rootDTO.getAddress())
                .course(course)
                .build();
            rootRepository.save(root);
        }
    }
    @Transactional
    public void deleteRoots(Long courseId){
        // Course 조회
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new AppException(CourseErrorCode.COURSE_NOT_FOUND));
        rootRepository.deleteByCourse(course);
    }
}
