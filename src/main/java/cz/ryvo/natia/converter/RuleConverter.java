package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.Rule;
import cz.ryvo.natia.domain.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

        Rule api = new Rule();
        api.setId(domain.getId());
        api.setName(domain.getName());
        api.setRank(domain.getRank());
        api.setInputArticles(ruleInputArticleConverter.toApi(domain.getInputArticles()));
        api.setOutputArticles(ruleOutputArticleConverter.toApi(domain.getOutputArticles()));
        return api;
    }

    public RuleVO toDomain(Rule api) {
        if (api == null) {
            return null;
        }

        RuleVO domain = new RuleVO();
        domain.setId(api.getId());
        domain.setName(api.getName());
        domain.setRank(api.getRank());
        domain.setInputArticles(ruleInputArticleConverter.toDomain(api.getInputArticles(), domain));
        domain.setOutputArticles(ruleOutputArticleConverter.toDomain(api.getOutputArticles(), domain));
        return domain;
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
