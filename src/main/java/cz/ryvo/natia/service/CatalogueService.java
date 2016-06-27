package cz.ryvo.natia.service;

import cz.ryvo.natia.domain.ArticleVO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CatalogueService {

    int importCatalogue(MultipartFile file);

    List<ArticleVO> searchArticles(@NotNull String pattern);

    ArticleVO getArticleByCode(@NotNull String code);
}
