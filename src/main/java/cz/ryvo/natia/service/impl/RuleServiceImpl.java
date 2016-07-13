package cz.ryvo.natia.service.impl;

import com.google.common.collect.ImmutableMap;
import cz.ryvo.natia.domain.*;
import cz.ryvo.natia.error.Errors.DUPLICATE_PRODUCT_CODE;
import cz.ryvo.natia.error.Errors.DUPLICATE_RULE_NAME;
import cz.ryvo.natia.error.Errors.INVALID_ITEM_RANK;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.exception.NotFoundException;
import cz.ryvo.natia.repository.RuleInputArticleRepository;
import cz.ryvo.natia.repository.RuleOutputArticleRepository;
import cz.ryvo.natia.repository.RuleRepository;
import cz.ryvo.natia.service.CatalogueService;
import cz.ryvo.natia.service.RuleService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

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
        return ruleRepository.findAllByOrderByRankAsc();
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

        RuleVO newRule = new RuleVO();
        newRule.setName(rule.getName());
        newRule.setRank(rule.getRank());

        Integer lastIndex = ruleRepository.getLastIndex();
        if (newRule.getRank() == null) {
            newRule.setRank(lastIndex == null ? 0 : ++lastIndex);
        } else {
            if (lastIndex == null) {
                newRule.setRank(0);
            } else if (newRule.getRank() <= lastIndex) {
                rerankRules(++lastIndex, newRule.getRank());
            } else {
                newRule.setRank(++lastIndex);
            }
        }

        Long ruleId = ruleRepository.save(newRule).getId();

        if (!isEmpty(rule.getInputArticles())) {
            rule.getInputArticles().forEach(p -> createInputArticle(ruleId, p));
        }

        if (!isEmpty(rule.getOutputArticles())) {
            rule.getOutputArticles().forEach(p -> createOutputArticle(ruleId, p));
        }

        return ruleId;
    }

    @Override
    public void updateRule(@NonNull Long id, @NonNull RuleVO rule) {
        RuleVO persistedRule = ruleRepository.findOne(id);
        if (persistedRule == null) {
            throw new NotFoundException("rule", id);
        }
        persistedRule.setName(rule.getName());
        persistedRule.getInputArticles().clear();
        persistedRule.getOutputArticles().clear();

        if (rule.getRank() != null) {
            rerankRules(persistedRule.getRank(), rule.getRank());
        }

        ruleRepository.save(persistedRule);

        if (!isEmpty(rule.getInputArticles())) {
            rule.getInputArticles().forEach(p -> createInputArticle(persistedRule.getId(), p));
        }

        if (!isEmpty(rule.getOutputArticles())) {
            rule.getOutputArticles().forEach(p -> createOutputArticle(persistedRule.getId(), p));
        }
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

        checkDuplicateRuleInputArticleCode(ruleId, article.getCode());

        RuleInputArticleVO inputArticle = new RuleInputArticleVO();
        inputArticle.setRule(rule);
        inputArticle.setCode(article.getCode());
        inputArticle.setDescription(article.getDescription());
        inputArticle.setAmount(article.getAmount());
        inputArticle.setInCatalogue(getIsArticleInCatalogue(inputArticle));
        inputArticle.setDescription(getRuleArticleDescription(inputArticle));

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

        checkDuplicateRuleOutputArticleCode(ruleId, article.getCode());

        RuleOutputArticleVO outputArticle = new RuleOutputArticleVO();
        outputArticle.setRule(rule);
        outputArticle.setCode(article.getCode());
        outputArticle.setDescription(article.getDescription());
        outputArticle.setAmount(article.getAmount());
        outputArticle.setInCatalogue(getIsArticleInCatalogue(outputArticle));
        outputArticle.setDescription(getRuleArticleDescription(outputArticle));

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

    private String getRuleArticleDescription(@NonNull RuleArticleVO ruleArticle) {
        ArticleVO article = catalogueService.getArticleByCode(ruleArticle.getCode());
        if (article != null && article.getDescription() != null) {
            return article.getDescription();
        }
        if (ruleArticle.getDescription() != null) {
            return ruleArticle.getDescription();
        }
        return "Unknown product";
    }

    private boolean getIsArticleInCatalogue(@NonNull RuleArticleVO ruleArticle) {
        ArticleVO article = catalogueService.getArticleByCode(ruleArticle.getCode());
        return article != null;
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
            throw new BadRequestException(DUPLICATE_RULE_NAME.class, singletonMap("name", name));
        }
    }

    private void checkDuplicateRuleInputArticleCode(Long ruleId, String code) {
        if (ruleInputArticleRepository.findOneByRuleIdAndCode(ruleId, code) != null) {
            throw new BadRequestException(DUPLICATE_PRODUCT_CODE.class, singletonMap("code", code));
        }
    }

    private void checkDuplicateRuleOutputArticleCode(Long ruleId, String code) {
        if (ruleOutputArticleRepository.findOneByRuleIdAndCode(ruleId, code) != null) {
            throw new BadRequestException(DUPLICATE_PRODUCT_CODE.class, singletonMap("code", code));
        }
    }
}