package cz.ryvo.natia.excel;

import cz.ryvo.natia.domain.OrderArticleVO;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;

public interface OrderReader {
    List<OrderArticleVO> readOrder(@Nonnull InputStream inputStream);
}
