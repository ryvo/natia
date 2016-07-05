package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.OrderArticleVO;
import cz.ryvo.natia.error.Errors;
import cz.ryvo.natia.excel.OrderReader;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.exception.InternalServerException;
import cz.ryvo.natia.service.OrderService;
import lombok.NonNull;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderReader orderReader;

    @Override
    public byte[] processOrder(@NonNull MultipartFile file) {
        List<OrderArticleVO> articles;
        try {
            articles = orderReader.readOrder(file.getInputStream());
            if (isEmpty(articles)) {
                throw new Exception("The order is empty.");
            }
        } catch (Exception e) {
            throw new BadRequestException(Errors.INVALID_FILE.class);
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        createOrderSummaryExcelSheet(workbook, articles);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
        byte[] result = out.toByteArray();

        try {
            FileOutputStream fout = new FileOutputStream(new File("c:\\25\\out.dat"));
            fout.write(result);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getFileExtension() {
        return ".xls";
    }

    private void createOrderSummaryExcelSheet(HSSFWorkbook workbook, List<OrderArticleVO> articles) {
        HSSFSheet sheet = workbook.createSheet("Order");
        int rowNum = 0;
        for(OrderArticleVO article : articles) {
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
            cell.setCellValue(article.getCode());
            cell = row.createCell(1, Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(article.getAmount());
        }
    }
}
