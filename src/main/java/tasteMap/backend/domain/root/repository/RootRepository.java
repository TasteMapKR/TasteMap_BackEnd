package tasteMap.backend.domain.root.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tasteMap.backend.domain.course.entity.Course;
import tasteMap.backend.domain.root.entity.Root;

import java.util.List;

@Repository
public interface RootRepository extends JpaRepository<Root, Long> {
    List<Root> findByCourse(Course course);

    void deleteByCourse(Course course);
}
