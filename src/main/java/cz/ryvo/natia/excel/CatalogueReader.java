package cz.ryvo.natia.excel;

import cz.ryvo.natia.domain.ArticleVO;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;

public interface CatalogueReader {

    List<ArticleVO> readCatalogue(@Nonnull InputStream inputStream);
}
