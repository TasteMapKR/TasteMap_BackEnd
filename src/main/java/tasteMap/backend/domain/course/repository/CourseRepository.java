package tasteMap.backend.domain.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tasteMap.backend.domain.course.entity.Course;
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
