package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.api.CreateResult;
import cz.ryvo.natia.api.Rule;
import cz.ryvo.natia.api.RuleArticle;
import cz.ryvo.natia.converter.RuleConverter;
import cz.ryvo.natia.converter.RuleInputArticleConverter;
import cz.ryvo.natia.converter.RuleOutputArticleConverter;
import cz.ryvo.natia.domain.RuleInputArticleVO;
import cz.ryvo.natia.domain.RuleOutputArticleVO;
import cz.ryvo.natia.domain.RuleVO;
import cz.ryvo.natia.service.RuleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController("v1RuleEndpoint")
@RequestMapping(path = RuleEndpoint.ENDPOINT_URL_PATH)
@Api(value = "v1.rule-endpoint")
public class RuleEndpoint {

    public final static String ENDPOINT_URL_PATH = "/api/v1/rules";

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleConverter ruleConverter;

    @Autowired
    private RuleInputArticleConverter ruleInputArticleConverter;

    @Autowired
    private RuleOutputArticleConverter ruleOutputArticleConverter;

    @RequestMapping(method = GET)
    public List<Rule> listRules() {
        return ruleConverter.toApi(ruleService.getRules());
    }

    @RequestMapping(path = "/{ruleId}", method = GET)
    public Rule getRule(@PathVariable("ruleId") Long id) {
        return ruleConverter.toApi(ruleService.getRule(id));
    }

    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public CreateResult createRule(@RequestBody @Valid Rule rule) {
        RuleVO ruleVO = ruleConverter.toDomain(rule);
        Long id = ruleService.createRule(ruleVO);
        return new CreateResult(id);
    }

    @RequestMapping(path = "/{ruleId}", method = PUT)
    public void updateRule(@PathVariable("ruleId") Long id, @RequestBody @Valid Rule rule) {
        RuleVO ruleVO = ruleConverter.toDomain(rule);
        ruleService.updateRule(id, ruleConverter.toDomain(rule));
    }

    @RequestMapping(path = "/{ruleId}", method = DELETE)
    public void deleteRule(@PathVariable("ruleId") Long ruleId) {
        ruleService.deleteRule(ruleId);
    }

    @RequestMapping(path = "/{ruleId}/rank/{rank}", method = PUT)
    public void setRuleRank(@PathVariable("ruleId") Long ruleId, @PathVariable("rank") Integer rank) {
        ruleService.setRuleRank(ruleId, rank);
    }

    @RequestMapping(path = "/{ruleId}/required-articles", method = GET)
    public List<RuleArticle> getRequiredArticles(@PathVariable("ruleId") Long ruleId) {
        return ruleInputArticleConverter.toApi(ruleService.getInputArticles(ruleId));
    }

    @RequestMapping(path = "/{ruleId}/gift-articles", method = GET)
    public List<RuleArticle> getGiftArticles(@PathVariable("ruleId") Long ruleId) {
        return ruleOutputArticleConverter.toApi(ruleService.getOutputArticles(ruleId));
    }

    @RequestMapping(path = "/{ruleId}/required-articles", method = POST)
    public CreateResult addRequiredArticle(@PathVariable("ruleId") Long ruleId, @RequestBody @Valid RuleArticle article) {
        RuleInputArticleVO articleVO = ruleInputArticleConverter.toDomain(article);
        Long id = ruleService.createInputArticle(ruleId, articleVO);
        return new CreateResult(id);
    }

    @RequestMapping(path = "/required-articles/{articleId}", method = DELETE)
    public void deleteRequiredArticle(@PathVariable("articleId") Long articleId) {
        ruleService.deleteInputArticle(articleId);
    }

    @RequestMapping(path = "/required-articles/{articleId}/amount/{amount}", method = PUT)
    public void updateRequiredArticleAmount(@PathVariable("articleId") Long articleId, @PathVariable("amount") Integer amount) {
        ruleService.updateInputArticleAmount(articleId, amount);
    }

    @RequestMapping(path = "/{ruleId}/gift-articles", method = POST)
    public CreateResult addGiftArticle(@PathVariable("ruleId") Long ruleId, @RequestBody @Valid RuleArticle article) {
        RuleOutputArticleVO articleVO = ruleOutputArticleConverter.toDomain(article);
        Long id = ruleService.createOutputArticle(ruleId, articleVO);
        return new CreateResult(id);
    }

    @RequestMapping(path = "/gift-articles/{articleId}", method = DELETE)
    public void deleteGiftArticle(@PathVariable("articleId") Long articleId) {
        ruleService.deleteOutputArticle(articleId);
    }

    @RequestMapping(path = "/gift-articles/{articleId}/amount/{amount}", method = PUT)
    public void updateGiftArticleAmount(@PathVariable("articleId") Long articleId, @PathVariable("amount") Integer amount) {
        ruleService.updateOutputArticleAmount(articleId, amount);
    }
}