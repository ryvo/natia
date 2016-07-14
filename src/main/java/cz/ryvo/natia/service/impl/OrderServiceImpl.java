package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.*;
import cz.ryvo.natia.error.Errors;
import cz.ryvo.natia.excel.OrderReader;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.service.OrderService;
import cz.ryvo.natia.service.RuleService;
import lombok.NonNull;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderReader orderReader;

    @Autowired
    private RuleService ruleService;

    @Override
    public List<GiftArticleVO> processOrder(@NonNull MultipartFile file) {
        Map<String, OrderArticleVO> articles = parseOrderedArticles(file).stream()
                .collect(Collectors.toMap(OrderArticleVO::getCode, Function.identity()));
        List<RuleVO> rules = ruleService.getRules();

        Map<String, GiftArticleVO> giftArticles = new HashMap<>();
        List<RuleVO> calculatedRules = new ArrayList<>();
        rules.forEach(r -> { // Iterate rules
            RuleVO rule = calculateRule(articles, r); // Calculate gifts for the rule
            if (rule != null) {
                rule.getOutputArticles().forEach(a -> { // Iterate rule output articles (calculated gifts)
                    GiftArticleVO giftArticle = giftArticles.get(a.getCode());
                    if (giftArticle != null) {
                        giftArticle.setPieces(giftArticle.getPieces() + a.getPieces());
                        giftArticle.setCode(a.getCode());
                        giftArticle.setDescription(a.getDescription());
                        giftArticle.setInCatalogue(a.getInCatalogue());
                        giftArticle.getRules().add(new GiftArticleRuleVO(r.getName(), a.getPieces()));
                    } else {
                        giftArticle = new GiftArticleVO();
                        giftArticle.setPieces(a.getPieces());
                        giftArticle.setCode(a.getCode());
                        giftArticle.setDescription(a.getDescription());
                        giftArticle.setInCatalogue(a.getInCatalogue());
                        giftArticle.getRules().add(new GiftArticleRuleVO(r.getName(), a.getPieces()));
                        giftArticles.put(a.getCode(), giftArticle);
                    }
                });
                calculatedRules.add(rule);
            }
        });

        return giftArticles.entrySet().stream().map(Map.Entry::getValue).collect(toList());
    }

    private List<OrderArticleVO> parseOrderedArticles(MultipartFile file) {
        List<OrderArticleVO> articles;
        try {
            articles = orderReader.readOrder(file.getInputStream());
            if (isEmpty(articles)) {
                throw new Exception("The order is empty.");
            }
        } catch (Exception e) {
            throw new BadRequestException(Errors.INVALID_FILE.class);
        }
        return articles;
    }

    private RuleVO calculateRule(Map<String, OrderArticleVO> articles, RuleVO rule) {
        // Find maximum multiplier
        int multiplier = Integer.MAX_VALUE;
        for (RuleInputArticleVO requiredArticle : rule.getInputArticles()) {
            OrderArticleVO orderedArticle = articles.get(requiredArticle.getCode());
            if (orderedArticle == null || orderedArticle.getPieces() == 0 || orderedArticle.getPieces() < requiredArticle.getPieces()) {
                return null; // The rule doesn't match
            }
            int i = orderedArticle.getPieces() / requiredArticle.getPieces();
            if (i < multiplier) {
                multiplier = i;
            }
        }

        // Apply maximum multiplier to gifts
        RuleVO calcRule = new RuleVO();
        calcRule.setId(rule.getId());
        calcRule.setName(rule.getName());
        calcRule.setRank(rule.getRank());
        calcRule.setOutputArticles(new ArrayList<>());
        for (RuleOutputArticleVO giftArticle : rule.getOutputArticles()) {
            RuleOutputArticleVO calcArticle = new RuleOutputArticleVO();
            calcArticle.setCode(giftArticle.getCode());
            calcArticle.setDescription(giftArticle.getDescription());
            calcArticle.setPieces(giftArticle.getPieces() * multiplier);
            calcArticle.setInCatalogue(giftArticle.getInCatalogue());
            calcRule.getOutputArticles().add(calcArticle);
        }
        return calcRule;
    }
}
