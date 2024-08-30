package tasteMap.backend.domain.course.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.domain.course.dto.response.CourseMainPageDTO;
import tasteMap.backend.domain.course.dto.response.CourseMyDTO;
import tasteMap.backend.domain.course.dto.response.CourseDetailDTO;
import tasteMap.backend.domain.course.dto.response.CourseOverview;
import tasteMap.backend.domain.course.repository.CourseRepository;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.repository.MemberRepository;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.MemberErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseApiService {
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;

    /**
     * MyPage용 페이징 나의 코스 찾기
     */
    public Page<CourseMyDTO> getCoursesByMemberId(String username, Pageable pageable) {
        // 사용자 이름으로 멤버 찾기
        Member member = memberRepository.findByUsername(username);
        if(member == null)
            throw new AppException(MemberErrorCode.MEMBER_NOT_FOUND);

        // 멤버 ID로 코스 조회
        return courseRepository.findCoursesByMemberId(member.getId(), pageable);
    }

    /**
     * 카테고리별로 조회
     */
    public Page<CourseMainPageDTO> getCoursesByCategory(String category, Pageable pageable) {
        return courseRepository.findCourseMainPageByCategory(category, pageable);
    }

    /**
     * 전체 조회
     */
    public Page<CourseMainPageDTO> getCourses(Pageable pageable) {
        return courseRepository.findCourseMainPage(pageable);
    }
    /**
     * 특정 코스 조회
     */
    public CourseOverview getCourseById(Long id){
        return courseRepository.findCourseById(id);
    }

}
