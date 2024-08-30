package tasteMap.backend.domain.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tasteMap.backend.domain.course.dto.response.CourseMainPageDTO;
import tasteMap.backend.domain.course.dto.response.CourseMyDTO;
import tasteMap.backend.domain.course.dto.response.CourseDetailDTO;
import tasteMap.backend.domain.course.dto.response.CourseOverview;

public interface CustomCourseRepository {
    Page<CourseMainPageDTO> findCourseMainPageByCategory(String category, Pageable pageable);
    Page<CourseMyDTO> findCoursesByMemberId(Long memberId, Pageable pageable);
    Page<CourseMainPageDTO> findCourseMainPage(Pageable pageable);
    CourseOverview findCourseById(Long id);
}