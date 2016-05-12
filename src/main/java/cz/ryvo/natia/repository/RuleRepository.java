package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.RuleVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<RuleVO, Long> {

    @Query("SELECT id, name FROM RuleVO")
    List<RuleVO> listAllRules();
}
