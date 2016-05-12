package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.RuleArticle;
import cz.ryvo.natia.domain.RuleInputArticleVO;
import cz.ryvo.natia.domain.RuleOutputArticleVO;
import cz.ryvo.natia.domain.RuleVO;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Converter
public class RuleOutputArticleConverter implements BiConverter<RuleArticle, RuleOutputArticleVO> {

    public @Nullable RuleArticle toApi(@Nullable RuleOutputArticleVO domain) {
        RuleArticle api = new RuleArticle();
        api.setCode(domain.getCode());
        api.setDescription(domain.getDescription());
        api.setAmount(domain.getAmount());
        return api;
    }

    public @Nullable RuleOutputArticleVO toDomain(@Nullable RuleArticle api) {
        RuleOutputArticleVO domain = new RuleOutputArticleVO();
        domain.setCode(api.getCode());
        domain.setDescription(api.getDescription());
        domain.setAmount(api.getAmount());
        return domain;
    }

    public @Nonnull List<RuleArticle> toApi(@Nullable List<RuleOutputArticleVO> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(this::toApi).collect(toList());
    }

    public @Nonnull List<RuleOutputArticleVO> toDomain(@Nullable List<RuleArticle> apis) {
        return toDomain(apis, null);
    }

    public @Nonnull List<RuleOutputArticleVO> toDomain(@Nullable List<RuleArticle> apis, @Nullable RuleVO rule) {
        if (CollectionUtils.isEmpty(apis)) {
            return Collections.emptyList();
        }
        return apis.stream().map(p -> {
            RuleOutputArticleVO ruleOutputArticleVO = toDomain(p);
            ruleOutputArticleVO.setRule(rule);
            return ruleOutputArticleVO;
        }).collect(toList());
    }
}
