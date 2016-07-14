package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.GiftArticleRule;
import cz.ryvo.natia.domain.GiftArticleRuleVO;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Converter
public class GiftArticleRuleConverter implements ApiConverter<GiftArticleRule, GiftArticleRuleVO> {

    @Override
    public GiftArticleRule toApi(GiftArticleRuleVO domain) {
        if (domain == null) {
            return null;
        }
        GiftArticleRule api = new GiftArticleRule();
        api.setName(domain.getName());
        api.setPieces(domain.getPieces());
        return api;
    }

    public List<GiftArticleRule> toApi(List<GiftArticleRuleVO> domains) {
        if (isEmpty(domains)) {
            return emptyList();
        }
        return domains.stream().map(this::toApi).collect(toList());
    }

}
