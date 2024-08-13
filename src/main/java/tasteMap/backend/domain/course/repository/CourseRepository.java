package tasteMap.backend.domain.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tasteMap.backend.domain.course.dto.response.CourseMainPageDTO;
import tasteMap.backend.domain.course.dto.response.CourseMyDTO;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.course.entity.Enum.Category;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT new tasteMap.backend.domain.course.dto.response.CourseMainPageDTO(c.id, c.title, " +
        "CAST(c.category AS string), m.name, m.profile_image) " +
        "FROM Course c JOIN c.member m " +
        "WHERE c.category = :category")
    Page<CourseMainPageDTO> findCourseMainPageByCategory(@Param("category") String category, Pageable pageable);
    @Query("SELECT new tasteMap.backend.domain.course.dto.response.CourseMyDTO(c.id, c.title) FROM Course c WHERE c.member.id = :memberId")
    Page<CourseMyDTO> findCoursesByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
