package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.Article;
import cz.ryvo.natia.domain.ArticleVO;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Converter
public class ArticleConverter implements ApiConverter<Article, ArticleVO> {

    public Article toApi(ArticleVO domain) {
        if (domain == null) {
            return null;
        }
        Article article = new Article();
        article.setCode(domain.getCode());
        article.setName(domain.getName());
        return article;
    }

    public List<Article> toApi(List<ArticleVO> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return emptyList();
        }

        return domains.stream().map(this::toApi).collect(toList());
    }
}
