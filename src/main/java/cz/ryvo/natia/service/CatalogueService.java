package cz.ryvo.natia.service;

import cz.ryvo.natia.domain.ArticleVO;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CatalogueService {

    void importCatalogue(MultipartFile file);

    List<ArticleVO> searchArticles(@NonNull String pattern);
}
