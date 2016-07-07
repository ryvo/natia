package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.RuleOutputArticleVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleOutputArticleRepository extends JpaRepository<RuleOutputArticleVO, Long> {

    List<RuleOutputArticleVO> findManyByRuleId(Long ruleId);

    RuleOutputArticleVO findOneByRuleIdAndCode(Long ruleId, String code);
}
