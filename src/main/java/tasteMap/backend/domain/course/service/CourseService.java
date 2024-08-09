package tasteMap.backend.domain.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tasteMap.backend.domain.course.dto.CourseDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.repository.CourseRepository;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    public Course save(CourseDTO courseDTO, String username){
        Member member = memberRepository.findByUsername(username);

        Course course = Course.builder()
            .title(courseDTO.getTitle())
            .content(courseDTO.getContent())
            .member(member)
            .build();

        return courseRepository.save(course);
    }
}
