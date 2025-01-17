package tasteMap.backend.domain.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.domain.course.dto.request.CourseDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.entity.Enum.Category;
import tasteMap.backend.domain.course.repository.CourseRepository;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.CourseErrorCode;
import tasteMap.backend.global.exception.errorCode.MemberErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public Course save(CourseDTO courseDTO, String username){
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(MemberErrorCode.MEMBER_NOT_FOUND));
        Category category = Category.valueOf(courseDTO.getCategory().toUpperCase());
        Course course = Course.builder()
            .title(courseDTO.getTitle())
            .content(courseDTO.getContent())
            .category(category)
            .member(member)
            .build();

        return courseRepository.save(course);
    }
    @Transactional
    public void delete(Long courseId, String username)
    {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new AppException(CourseErrorCode.COURSE_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 해당 Course가 현재 사용자에 의해 소유되고 있는지 확인
        if (course.getMember().equals(member)) {
            courseRepository.deleteById(courseId);
        } else {
            throw new AppException(CourseErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
    @Transactional
    public Course update(Long courseId, CourseDTO courseDTO, String username) {

        // 기존 Course 객체를 조회
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new AppException(CourseErrorCode.COURSE_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(MemberErrorCode.MEMBER_NOT_FOUND));


        // 해당 Course가 현재 사용자에 의해 소유되고 있는지 확인
        if (course.getMember().equals(member)) {
            course.setTitle(courseDTO.getTitle());
            course.setContent(courseDTO.getContent());
            course.setCategory(Category.valueOf(courseDTO.getCategory().toUpperCase()));
            return courseRepository.save(course);
        } else {
            throw new AppException(CourseErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
