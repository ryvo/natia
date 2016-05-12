package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.RuleVO;
import cz.ryvo.natia.exception.NotFoundException;
import cz.ryvo.natia.repository.RuleRepository;
import cz.ryvo.natia.service.RuleService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        return ruleRepository.save(rule).getId();
    }

    @Override
    public void updateRule(@NonNull Long id, @NonNull RuleVO rule) {
        RuleVO persistedRule = ruleRepository.findOne(id);
        if (rule == null) {
            throw new NotFoundException("rule", id);
        }
        persistedRule.setName(rule.getName());
        persistedRule.getInputArticles().clear();
        persistedRule.setInputArticles(rule.getInputArticles());
        persistedRule.getOutputArticles().clear();
        persistedRule.setOutputArticles(rule.getOutputArticles());
    }

    @Override
    public void deleteRule(@NonNull Long id) {
        ruleRepository.delete(id);
    }
}
