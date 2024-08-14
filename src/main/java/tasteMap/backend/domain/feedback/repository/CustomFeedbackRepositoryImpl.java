package tasteMap.backend.domain.feedback.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import tasteMap.backend.domain.course.dto.response.CourseMainPageDTO;
import tasteMap.backend.domain.course.entity.Enum.Category;
import tasteMap.backend.domain.feedback.dto.response.FeedbackCountsDto;
import tasteMap.backend.domain.feedback.dto.response.FeedbackResponseDTO;
import tasteMap.backend.domain.feedback.entity.QFeedback;
import tasteMap.backend.domain.member.entity.Member;
import tasteMap.backend.domain.member.entity.QMember;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class CustomFeedbackRepositoryImpl implements CustomFeedbackRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<FeedbackResponseDTO> findFeedbackDTOByRootID(Long rootId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QFeedback feedback = QFeedback.feedback;
        QMember member = QMember.member;

        List<FeedbackResponseDTO> results = queryFactory
            .select(Projections.constructor(FeedbackResponseDTO.class,
                feedback.status,
                feedback.content,
                member.name,
                member.profile_image))
            .from(feedback)
            .join(feedback.member, member)
            .where(feedback.rootId.eq(rootId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(feedback.count())
            .from(feedback)
            .where(feedback.rootId.eq(rootId))
            .fetchOne();
        long totalCount = (total != null) ? total : 0;

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public FeedbackResponseDTO findMyFeedback(Long rootId, Member member) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QFeedback feedback = QFeedback.feedback;
        QMember qMember = QMember.member;

        return queryFactory
            .select(Projections.constructor(FeedbackResponseDTO.class,
                feedback.status,
                feedback.content,
                qMember.name,
                qMember.profile_image))
            .from(feedback)
            .join(feedback.member, qMember)
            .where(feedback.rootId.eq(rootId)
                .and(feedback.member.eq(member)))
            .fetchOne();
    }

    @Override
    public FeedbackCountsDto countFeedbacksByRootId(Long rootId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QFeedback feedback = QFeedback.feedback;

        // 집계 쿼리 작성
        List<Tuple> results = queryFactory
            .select(
                feedback.status,
                feedback.count()
            )
            .from(feedback)
            .where(feedback.rootId.eq(rootId))
            .groupBy(feedback.status)
            .fetch();

        // 집계 결과를 FeedbackCountsDto로 변환
        long positiveCount = 0;
        long negativeCount = 0;

        for (Tuple tuple : results) {
            boolean status = Boolean.TRUE.equals(tuple.get(feedback.status));
            long count = tuple.get(feedback.count()) == null ? 0 : tuple.get(feedback.count());
            if (status) {
                positiveCount = count;
            } else {
                negativeCount = count;
            }
        }

        return new FeedbackCountsDto(positiveCount, negativeCount);
    }
}
