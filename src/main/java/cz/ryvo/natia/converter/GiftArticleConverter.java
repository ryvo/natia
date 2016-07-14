package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.GiftArticle;
import cz.ryvo.natia.domain.GiftArticleVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Converter
public class GiftArticleConverter implements ApiConverter<GiftArticle, GiftArticleVO> {

    @Autowired
    private GiftArticleRuleConverter giftArticleRuleConverter;

    @Override
    public GiftArticle toApi(GiftArticleVO domain) {
        if (domain == null) {
            return null;
        }
        GiftArticle api = new GiftArticle();
        api.setCode(domain.getCode());
        api.setDescription(domain.getDescription());
        api.setPieces(domain.getPieces());
        api.setInCatalogue(domain.getInCatalogue());
        api.setRules(giftArticleRuleConverter.toApi(domain.getRules()));
        return api;
    }

    public List<GiftArticle> toApi(List<GiftArticleVO> domains) {
        if (isEmpty(domains)) {
            return emptyList();
        }
        return domains.stream().map(this::toApi).collect(toList());
    }
}
