package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.api.CreateResult;
import cz.ryvo.natia.api.Rule;
import cz.ryvo.natia.converter.RuleConverter;
import cz.ryvo.natia.domain.RuleVO;
import cz.ryvo.natia.service.RuleService;
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

@RestController
@RequestMapping(path = RuleEndpoint.ENDPOINT_URL_PATH)
public class RuleEndpoint {

    public final static String ENDPOINT_URL_PATH = "/api/v1/rules";

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleConverter ruleConverter;

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

    @RequestMapping(path = "/{ruleId}/order/{order}", method = PUT)
    public void setRuleOrder(@PathVariable("ruleId") Long ruleId, @PathVariable("order") Integer order) {
        ruleService.setRuleIndex(ruleId, order);
    }
}