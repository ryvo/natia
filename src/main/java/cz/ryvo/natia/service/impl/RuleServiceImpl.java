package cz.ryvo.natia.service.impl;

import com.google.common.collect.ImmutableMap;
import cz.ryvo.natia.domain.ArticleVO;
import cz.ryvo.natia.domain.RuleInputArticleVO;
import cz.ryvo.natia.domain.RuleOutputArticleVO;
import cz.ryvo.natia.domain.RuleVO;
import cz.ryvo.natia.error.Errors;
import cz.ryvo.natia.error.Errors.INVALID_ITEM_RANK;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.exception.NotFoundException;
import cz.ryvo.natia.repository.ArticleRepository;
import cz.ryvo.natia.repository.RuleInputArticleRepository;
import cz.ryvo.natia.repository.RuleOutputArticleRepository;
import cz.ryvo.natia.repository.RuleRepository;
import cz.ryvo.natia.service.CatalogueService;
import cz.ryvo.natia.service.RuleService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

@Service
@Transactional
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private RuleInputArticleRepository ruleInputArticleRepository;

    @Autowired
    private RuleOutputArticleRepository ruleOutputArticleRepository;

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
        requireNonNull(rule.getName(), "Rule name must not be null");

        checkDuplicateRuleName(rule.getName());

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
    public void setRuleRank(@NonNull Long id, @NonNull Integer newRank) {
        Integer lastRank = ruleRepository.getLastIndex();
        if (newRank < 0 || newRank > lastRank + 1) {
            throw new BadRequestException(INVALID_ITEM_RANK.class, ImmutableMap.of("rank", newRank, "resource", "rule"));
        }

        RuleVO persistedRule = ruleRepository.findOne(id);
        if (persistedRule == null) {
            throw new NotFoundException("rule", id);
        }

        if (persistedRule.getRank().equals(newRank)) {
            return; // No action needed
        }

        rerankRules(persistedRule.getRank(), newRank);
        persistedRule.setRank(newRank);
    }

    @Override
    public List<RuleInputArticleVO> getInputArticles(@NonNull Long ruleId) {
        return ruleInputArticleRepository.findManyByRuleId(ruleId);
    }

    @Override
    public List<RuleOutputArticleVO> getOutputArticles(@NonNull Long ruleId) {
        return ruleOutputArticleRepository.findManyByRuleId(ruleId);
    }

    @Override
    public Long createInputArticle(@NonNull Long ruleId, @NonNull RuleInputArticleVO article) {
        requireNonNull(article.getCode(), "article.code must not be null");

        RuleVO rule = ruleRepository.findOne(ruleId);
        if (rule == null) {
            throw new NotFoundException("rule", ruleId);
        }

        RuleInputArticleVO inputArticle = new RuleInputArticleVO();
        inputArticle.setRule(rule);
        inputArticle.setCode(article.getCode());
        inputArticle.setAmount(1);

        ArticleVO tmpArticle = catalogueService.getArticleByCode(article.getCode());
        inputArticle.setInCatalogue(tmpArticle != null);

        if (tmpArticle != null) {
            inputArticle.setDescription(tmpArticle.getDescription());
        } else {
            if (article.getDescription() != null) {
                inputArticle.setDescription(article.getDescription());
            } else {
                inputArticle.setDescription("Unknown product");
            }
        }

        return ruleInputArticleRepository.save(inputArticle).getId();
    }

    @Override
    public void deleteInputArticle(@NonNull Long articleId) {
        RuleInputArticleVO article = ruleInputArticleRepository.findOne(articleId);
        if (article != null) {
            ruleInputArticleRepository.delete(article);
        }
    }

    @Override
    public void updateInputArticleAmount(@NonNull Long articleId, @NonNull Integer amount) {
        RuleInputArticleVO article = ruleInputArticleRepository.findOne(articleId);
        if (article == null) {
            throw new NotFoundException("inputArticle", articleId);
        }
        article.setAmount(amount);
        ruleInputArticleRepository.save(article);
    }

    @Override
    public Long createOutputArticle(@NonNull Long ruleId, @NonNull RuleOutputArticleVO article) {
        requireNonNull(article.getCode(), "article.code must not be null");

        RuleVO rule = ruleRepository.findOne(ruleId);
        if (rule == null) {
            throw new NotFoundException("rule", ruleId);
        }

        RuleOutputArticleVO outputArticle = new RuleOutputArticleVO();
        outputArticle.setRule(rule);
        outputArticle.setCode(article.getCode());
        outputArticle.setAmount(1);

        ArticleVO tmpArticle = catalogueService.getArticleByCode(article.getCode());
        outputArticle.setInCatalogue(tmpArticle != null);

        if (tmpArticle != null) {
            outputArticle.setDescription(tmpArticle.getDescription());
        } else {
            if (article.getDescription() != null) {
                outputArticle.setDescription(article.getDescription());
            } else {
                outputArticle.setDescription("Unknown product");
            }
        }

        return ruleOutputArticleRepository.save(outputArticle).getId();
    }

    @Override
    public void deleteOutputArticle(@NonNull Long articleId) {
        RuleOutputArticleVO article = ruleOutputArticleRepository.findOne(articleId);
        if (article != null) {
            ruleOutputArticleRepository.delete(article);
        }
    }

    @Override
    public void updateOutputArticleAmount(@NonNull Long articleId, @NonNull Integer amount) {
        RuleOutputArticleVO article = ruleOutputArticleRepository.findOne(articleId);
        if (article == null) {
            throw new NotFoundException("outputArticle", articleId);
        }
        article.setAmount(amount);
        ruleOutputArticleRepository.save(article);
    }

    private void rerankRules(@NonNull Integer oldIndex, @NonNull Integer newIndex) {
        if (newIndex < oldIndex) {
            ruleRepository.moveRuleIndexDown(oldIndex, newIndex);
        } else if (newIndex > oldIndex) {
            ruleRepository.moveRuleIndexUp(oldIndex, newIndex);
        }
    }

    private void checkDuplicateRuleName(String name) {
        if (ruleRepository.findOneByName(name) != null) {
            throw new BadRequestException(Errors.DUPLICATE_RULE_NAME.class, singletonMap("name", name));
        }
    }
}