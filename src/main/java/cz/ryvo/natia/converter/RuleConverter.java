package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.Rule;
import cz.ryvo.natia.domain.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Converter
public class RuleConverter implements BiConverter<Rule, RuleVO> {

    @Autowired
    private RuleInputArticleConverter ruleInputArticleConverter;

    @Autowired
    private RuleOutputArticleConverter ruleOutputArticleConverter;

    public Rule toApi(RuleVO domain) {
        if (domain == null) {
            return null;
        }

        Rule rule = new Rule();
        rule.setId(domain.getId());
        rule.setName(domain.getName());
        rule.setInputArticles(ruleInputArticleConverter.toApi(domain.getInputArticles()));
        rule.setOutputArticles(ruleOutputArticleConverter.toApi(domain.getOutputArticles()));
        return rule;
    }

    public RuleVO toDomain(Rule api) {
        if (api == null) {
            return null;
        }

        RuleVO rule = new RuleVO();
        rule.setId(api.getId());
        rule.setName(api.getName());
        rule.setInputArticles(ruleInputArticleConverter.toDomain(api.getInputArticles(), rule));
        rule.setOutputArticles(ruleOutputArticleConverter.toDomain(api.getOutputArticles(), rule));
        return rule;
    }

    public List<Rule> toApi(List<RuleVO> domains) {
        if (domains == null) {
            return emptyList();
        }
        return domains.stream().map(this::toApi).collect(toList());
    }

    public List<RuleVO> toDomain(List<Rule> apis) {
        if (apis == null) {
            return emptyList();
        }
        return apis.stream().map(this::toDomain).collect(toList());
    }
}
