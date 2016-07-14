package cz.ryvo.natia.service;

import cz.ryvo.natia.domain.RuleInputArticleVO;
import cz.ryvo.natia.domain.RuleOutputArticleVO;
import cz.ryvo.natia.domain.RuleVO;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.List;

public interface RuleService {

    List<RuleVO> getRules();

    RuleVO getRule(@Nonnull Long id);

    Long createRule(@Nonnull RuleVO rule);

    void updateRule(@Nonnull Long id, @Nonnull RuleVO rule);

    void deleteRule(@Nonnull Long id);

    void setRuleRank(@NonNull Long id, @NonNull Integer newOrder);

    List<RuleInputArticleVO> getInputArticles(@NonNull Long id);

    List<RuleOutputArticleVO> getOutputArticles(@NonNull Long id);

    Long createInputArticle(@NonNull Long ruleId, @NonNull RuleInputArticleVO article);

    void deleteInputArticle(@NonNull Long articleId);

    void updateInputArticlePieces(@NonNull Long articleId, @NonNull Integer amount);

    Long createOutputArticle(@NonNull Long ruleId, @NonNull RuleOutputArticleVO article);

    void deleteOutputArticle(@NonNull Long articleId);

    void updateOutputArticlePieces(@NonNull Long articleId, @NonNull Integer amount);
}
