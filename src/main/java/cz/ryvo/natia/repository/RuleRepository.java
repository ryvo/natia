package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.RuleVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<RuleVO, Long> {

    List<RuleVO> findAllByOrderByRankAsc();

    @Query("SELECT max(rank) FROM RuleVO")
    Integer getLastIndex();

    @Modifying
    @Query("UPDATE RuleVO SET rank = rank + 1 WHERE rank >= :newRank AND rank < :oldIndex")
    void moveRuleIndexDown(@Param("oldIndex") Integer oldRank, @Param("newRank") Integer newRank);

    @Modifying
    @Query("UPDATE RuleVO SET rank = rank - 1 WHERE rank > :oldRank AND rank <= :newRank")
    void moveRuleIndexUp(@Param("oldRank") Integer oldRank, @Param("newRank") Integer newRank);

    RuleVO findOneByName(String name);
}
