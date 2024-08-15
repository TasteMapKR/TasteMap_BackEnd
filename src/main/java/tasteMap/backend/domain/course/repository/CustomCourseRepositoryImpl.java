package tasteMap.backend.domain.course.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import tasteMap.backend.domain.course.dto.response.CourseMainPageDTO;
import tasteMap.backend.domain.course.dto.response.CourseMyDTO;
import tasteMap.backend.domain.course.entity.Enum.Category;
import tasteMap.backend.domain.course.entity.QCourse;
import tasteMap.backend.domain.member.entity.QMember;
import java.util.List;

@Repository
public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    @Autowired
    private EntityManager em;

    @Override
    public Page<CourseMainPageDTO> findCourseMainPageByCategory(String category, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCourse course = QCourse.course;
        QMember member = QMember.member;

        List<CourseMainPageDTO> results = queryFactory
            .select(Projections.constructor(CourseMainPageDTO.class,
                course.id,
                course.title,
                course.category.stringValue(),
                member.name,
                member.profile_image))
            .from(course)
            .join(course.member, member)
            .where(course.category.eq(Category.valueOf(category)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(course.count()).
            from(course)
            .where(course.category.eq(Category.valueOf(category)))
            .fetchOne();
        long totalCount = (total != null) ? total : 0;

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<CourseMyDTO> findCoursesByMemberId(Long memberId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QCourse course = QCourse.course;

        List<CourseMyDTO> results = queryFactory
            .select(Projections.constructor(CourseMyDTO.class,
                course.id,
                course.title))
            .from(course)
            .where(course.member.id.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(course.count())
            .from(course)
            .where(course.member.id.eq(memberId))
            .fetchOne();
        long totalCount = (total != null) ? total : 0;
        return new PageImpl<>(results, pageable, totalCount);
    }
}