package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.*;
import cz.ryvo.natia.error.Errors;
import cz.ryvo.natia.excel.OrderReader;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.exception.InternalServerException;
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

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderReader orderReader;

    @Autowired
    private RuleService ruleService;

    @Override
    public byte[] processOrder(@NonNull MultipartFile file) {
        Map<String, OrderArticleVO> articles = parseOrderedArticles(file).stream()
                .collect(Collectors.toMap(OrderArticleVO::getCode, Function.identity()));
        List<RuleVO> rules = ruleService.getRules();

        List<RuleVO> calculatedRules = new ArrayList<>();
        rules.forEach(p -> {
            RuleVO rule = calculateRule(articles, p);
            if (rule != null) {
                calculatedRules.add(rule);
            }
        });

        List<RuleOutputArticleVO> summarizedGifts = summarizeGifts(calculatedRules);

        HSSFWorkbook workbook = new HSSFWorkbook();
        createGiftSummaryExcelSheet(workbook, summarizedGifts);
        createRuleOverviewExcelSheet(workbook, calculatedRules);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
        return out.toByteArray();
    }

    @Override
    public String getFileExtension() {
        return ".xls";
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
            if (orderedArticle == null || orderedArticle.getAmount() == 0 || orderedArticle.getAmount() < requiredArticle.getAmount()) {
                return null; // The rule doesn't match
            }
            int i = orderedArticle.getAmount() / requiredArticle.getAmount();
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
            calcArticle.setAmount(giftArticle.getAmount() * multiplier);
            calcRule.getOutputArticles().add(calcArticle);
        }
        return calcRule;
    }

    private List<RuleOutputArticleVO> summarizeGifts(List<RuleVO> calculatedRules) {
        Map<String, RuleOutputArticleVO> articleMap = new HashMap<>();
        calculatedRules.forEach(p -> {
            List<RuleOutputArticleVO> articles = p.getOutputArticles();
            articles.forEach(a -> {
                RuleOutputArticleVO article = articleMap.get(a.getCode());
                if (article == null) {
                    article = new RuleOutputArticleVO();
                    article.setCode(a.getCode());
                    article.setDescription(a.getDescription());
                    article.setAmount(a.getAmount());
                    articleMap.put(a.getCode(), article);
                } else {
                    article.setAmount(article.getAmount() + a.getAmount());
                }
            });
        });

        List<RuleOutputArticleVO> articleList = new ArrayList<>();
        articleMap.forEach((k, v) -> articleList.add(v));
        return articleList;
    }

    private void createGiftSummaryExcelSheet(HSSFWorkbook workbook, List<RuleOutputArticleVO> gifts) {
        HSSFSheet sheet = workbook.createSheet("Gifts");
        int nextRow = createGiftSummaryHeader(workbook, sheet);
        createGiftSummaryValues(workbook, sheet, gifts, nextRow);
    }

    private int createGiftSummaryHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
        int rowNum = 0;
        // Table title
        Row row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("CALCULATED GIFTS");
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
        // Column titles
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("Code");
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        cell = row.createCell(1, Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue("Amount");
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        cell = row.createCell(2, Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue("Description");
        return rowNum;
    }

    private void createGiftSummaryValues(HSSFWorkbook workbook, HSSFSheet sheet, List<RuleOutputArticleVO> gifts, int firstRow) {
        int rowNum = firstRow;
        for(RuleOutputArticleVO article : gifts) {
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
            cell.setCellValue(article.getCode());
            CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
            cell = row.createCell(1, Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(article.getAmount());
            CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
            cell = row.createCell(2, Cell.CELL_TYPE_STRING);
            cell.setCellValue(article.getDescription());
        }
    }

    private void createRuleOverviewExcelSheet(HSSFWorkbook workbook, List<RuleVO> calculatedRules) {
        HSSFSheet sheet = workbook.createSheet("Rules");
        int nextRow = createRuleOverviewHeader(workbook, sheet);
        createRuleOverviewValues(workbook, sheet, calculatedRules, nextRow);
    }

    private int createRuleOverviewHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
        int rowNum = 0;
        // Table title
        Row row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("CALCULATED RULES OVERVIEW");
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
        // Column titles
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("Rule");
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("Product code");
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        cell = row.createCell(2, Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue("Amount");
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        cell = row.createCell(3, Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue("Description");
        return rowNum;
    }

    private void createRuleOverviewValues(HSSFWorkbook workbook, HSSFSheet sheet, List<RuleVO> calculatedRules, int firstRow) {
        int rowNum = firstRow;
        for(RuleVO rule : calculatedRules) {
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
            cell.setCellValue(rule.getName());
            for(RuleOutputArticleVO article : rule.getOutputArticles()) {
                row = sheet.createRow(rowNum++);
                cell = row.createCell(1, Cell.CELL_TYPE_STRING);
                cell.setCellValue(article.getCode());
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
                cell = row.createCell(2, Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(article.getAmount());
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
                cell = row.createCell(3, Cell.CELL_TYPE_STRING);
                cell.setCellValue(article.getDescription());
            }
        }
    }
}
