package tasteMap.backend.domain.course.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.domain.course.dto.CourseDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.repository.CourseRepository;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.MemberErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public Course save(CourseDTO courseDTO, String username){
        Member member = memberRepository.findByUsername(username);

        Course course = Course.builder()
            .title(courseDTO.getTitle())
            .content(courseDTO.getContent())
            .member(member)
            .build();

        return courseRepository.save(course);
    }
    @Transactional
    public void delete(Long courseId){
        courseRepository.deleteById(courseId);
    }
    @Transactional
    public Course update(Long courseId, CourseDTO courseDTO) {

        // 기존 Course 객체를 조회
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new AppException(MemberErrorCode.MEMBER_NOT_FOUND));

        // Course 객체의 속성을 업데이트
        course.setTitle(courseDTO.getTitle());
        course.setContent(courseDTO.getContent());

        return courseRepository.save(course);
    }
}
