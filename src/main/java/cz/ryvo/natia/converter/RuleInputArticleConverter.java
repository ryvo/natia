package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.RuleArticle;
import cz.ryvo.natia.domain.RuleInputArticleVO;
import cz.ryvo.natia.domain.RuleVO;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Converter
public class RuleInputArticleConverter implements BiConverter<RuleArticle, RuleInputArticleVO> {

    public @Nullable RuleArticle toApi(@Nullable RuleInputArticleVO domain) {
        if (domain == null) {
            return null;
        }
        RuleArticle api = new RuleArticle();
        api.setId(domain.getId());
        api.setCode(domain.getCode());
        api.setDescription(domain.getDescription());
        api.setPieces(domain.getPieces());
        api.setInCatalogue(domain.getInCatalogue());
        return api;
    }

    public @Nullable RuleInputArticleVO toDomain(@Nullable RuleArticle api) {
        if (api == null) {
            return null;
        }
        RuleInputArticleVO domain = new RuleInputArticleVO();
        domain.setCode(api.getCode());
        domain.setDescription(api.getDescription());
        domain.setPieces(api.getPieces());
        return domain;
    }

    public @Nonnull List<RuleArticle> toApi(@Nullable List<RuleInputArticleVO> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(this::toApi).collect(toList());
    }

    public @Nonnull List<RuleInputArticleVO> toDomain(@Nullable List<RuleArticle> apis) {
        return toDomain(apis, null);
    }

    public @Nonnull List<RuleInputArticleVO> toDomain(@Nullable List<RuleArticle> apis, @Nullable RuleVO rule) {
        if (CollectionUtils.isEmpty(apis)) {
            return Collections.emptyList();
        }
        return apis.stream().map(p -> {
            RuleInputArticleVO ruleInputArticleVO = toDomain(p);
            ruleInputArticleVO.setRule(rule);
            return ruleInputArticleVO;
        }).collect(toList());
    }
}
