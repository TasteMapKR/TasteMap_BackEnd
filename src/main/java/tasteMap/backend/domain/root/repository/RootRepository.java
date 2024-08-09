package tasteMap.backend.domain.root.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tasteMap.backend.domain.root.entity.Root;

@Repository
public interface RootRepository extends JpaRepository<Root, Long> {
}
