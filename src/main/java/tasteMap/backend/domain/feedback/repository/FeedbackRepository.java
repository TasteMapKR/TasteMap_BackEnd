package tasteMap.backend.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tasteMap.backend.domain.feedback.entity.Feedback;
import tasteMap.backend.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, CustomFeedbackRepository {
    boolean existsByRootIdAndMember(Long rootId, Member member);

    Optional<Feedback> findByRootIdAndMember(Long id, Member member);

    void deleteByIdAndMember(Long id, Member member);

    Long countByRootIdAndStatus(Long rootId, boolean status);

}
