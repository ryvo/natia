package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.RuleInputArticleVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleInputArticleRepository extends JpaRepository<RuleInputArticleVO, Long> {

    List<RuleInputArticleVO> findManyByRuleId(Long ruleId);
}
