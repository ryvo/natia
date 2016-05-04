package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.ArticleVO;
import cz.ryvo.natia.error.Errors;
import cz.ryvo.natia.error.Errors.SEARCH_PARAMS_TOO_INACCURATE;
import cz.ryvo.natia.excel.CatalogueReader;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.repository.ArticleRepository;
import cz.ryvo.natia.service.CatalogueService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Transactional(readOnly = true)
public class CatalogueServiceImpl implements CatalogueService {

    @Autowired
    private CatalogueReader catalogueReader;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    @Transactional
    public void importCatalogue(@NonNull MultipartFile file) {
        List<ArticleVO> articles;
        try {
            articles = catalogueReader.readCatalogue(file.getInputStream());
            if (isEmpty(articles)) {
                throw new Exception("Empty catalogue, no articles found. Cancelling operation.");
            }
        } catch (Exception e) {
            throw new BadRequestException(Errors.INVALID_FILE.class);
        }

        articles.forEach(articleRepository::save);
    }

    @Override
    public List<ArticleVO> searchArticles(@NonNull String pattern) {
        if (pattern.length() > 3) {
            return articleRepository.findManyByPattern(pattern);
        } else {
            throw new BadRequestException(SEARCH_PARAMS_TOO_INACCURATE.class);
        }
    }
}
