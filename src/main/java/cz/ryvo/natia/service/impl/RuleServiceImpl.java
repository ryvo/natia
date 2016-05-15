package cz.ryvo.natia.service.impl;

import com.google.common.collect.ImmutableMap;
import cz.ryvo.natia.domain.RuleVO;
import cz.ryvo.natia.error.Errors.INVALID_ITEM_RANK;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.exception.NotFoundException;
import cz.ryvo.natia.repository.RuleRepository;
import cz.ryvo.natia.service.RuleService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public List<RuleVO> getRules() {
        return ruleRepository.listAllRules();
    }

    @Override
    public RuleVO getRule(@NonNull Long id) {
        RuleVO rule = ruleRepository.findOne(id);
        if (rule == null) {
            throw new NotFoundException("rule", id);
        }
        return rule;
    }

    @Override
    public Long createRule(@NonNull RuleVO rule) {
        Integer lastIndex = ruleRepository.getLastIndex();
        if (rule.getRank() == null) {
            rule.setRank(lastIndex == null ? 0 : ++lastIndex);
        } else {
            if (lastIndex == null) {
                rule.setRank(0);
            } else if (rule.getRank() <= lastIndex) {
                rerankRules(++lastIndex, rule.getRank());
            } else {
                rule.setRank(++lastIndex);
            }
        }
        return ruleRepository.save(rule).getId();
    }

    @Override
    public void updateRule(@NonNull Long id, @NonNull RuleVO rule) {
        RuleVO persistedRule = ruleRepository.findOne(id);
        if (persistedRule == null) {
            throw new NotFoundException("rule", id);
        }
        persistedRule.setName(rule.getName());

        persistedRule.getInputArticles().clear();
        rule.getInputArticles().forEach(p -> p.setRule(persistedRule));
        persistedRule.getInputArticles().addAll(rule.getInputArticles());

        persistedRule.getOutputArticles().clear();
        rule.getOutputArticles().forEach(p -> p.setRule(persistedRule));
        persistedRule.getOutputArticles().addAll(rule.getOutputArticles());

        if (rule.getRank() != null) {
            rerankRules(persistedRule.getRank(), rule.getRank());
        }

        ruleRepository.save(persistedRule);
    }

    @Override
    public void deleteRule(@NonNull Long id) {
        ruleRepository.delete(id);
    }

    @Override
    public void setRuleIndex(@NonNull Long id, @NonNull Integer newIndex) {
        Integer lastIndex = ruleRepository.getLastIndex();
        if (newIndex < 0 || newIndex > lastIndex + 1) {
            throw new BadRequestException(INVALID_ITEM_RANK.class, ImmutableMap.of("rank", newIndex, "resource", "rule"));
        }

        RuleVO persistedRule = ruleRepository.findOne(id);
        if (persistedRule == null) {
            throw new NotFoundException("rule", id);
        }

        if (persistedRule.getRank().equals(newIndex)) {
            return; // No action needed
        }

        rerankRules(persistedRule.getRank(), newIndex);
        persistedRule.setRank(newIndex);
    }

    private void rerankRules(@NonNull Integer oldIndex, @NonNull Integer newIndex) {
        if (newIndex < oldIndex) {
            ruleRepository.moveRuleIndexDown(oldIndex, newIndex);
        } else if (newIndex > oldIndex) {
            ruleRepository.moveRuleIndexUp(oldIndex, newIndex);
        }
    }
}
