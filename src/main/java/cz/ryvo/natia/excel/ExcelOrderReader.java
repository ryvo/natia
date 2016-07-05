package cz.ryvo.natia.excel;

import cz.ryvo.natia.domain.OrderArticleVO;
import cz.ryvo.natia.error.Errors;
import cz.ryvo.natia.exception.BadRequestException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ExcelOrderReader implements OrderReader {

    private final Pattern namePattern = Pattern.compile(".+");
    private final Pattern codePattern = Pattern.compile("\\d{3,}(/\\d{1,})?");
    private final Pattern amountPattern = Pattern.compile("[0-9]{1,}");

    @Override
    public List<OrderArticleVO> readOrder(@Nonnull InputStream inputStream) {
        HSSFWorkbook document;
        try {
            document = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new BadRequestException(Errors.INVALID_FILE.class);
        }

        HSSFSheet sheet = document.getSheet("Price");
        if (sheet == null) {
            return Collections.emptyList();
        }

        List<OrderArticleVO> articles = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        for (Row row : sheet) {
            Cell cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String code = formatter.formatCellValue(cell).trim();
            cell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String name = formatter.formatCellValue(cell).trim();
            cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String strAmount = formatter.formatCellValue(cell).trim();

            if (namePattern.matcher(name).matches() && codePattern.matcher(code).matches() && amountPattern.matcher(strAmount).matches()) {
                OrderArticleVO article = new OrderArticleVO();
                article.setCode(code);
                article.setDescription(name);
                try {
                    article.setAmount(Integer.parseInt(strAmount));
                } catch (NumberFormatException e) {
                    continue;
                }
                articles.add(article);
            }
        }
        return articles;
    }
}
