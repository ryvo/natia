package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.ArticleVO;
import cz.ryvo.natia.domain.ParameterVO.ParameterEnum;
import cz.ryvo.natia.error.Errors.INVALID_FILE;
import cz.ryvo.natia.error.Errors.SEARCH_PARAMS_TOO_INACCURATE;
import cz.ryvo.natia.excel.CatalogueReader;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.repository.ArticleRepository;
import cz.ryvo.natia.service.CatalogueService;
import cz.ryvo.natia.service.ParameterService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cz.ryvo.natia.domain.ParameterVO.ParameterEnum.FILENAME_OF_IMPORTED_CATALOGUE;
import static cz.ryvo.natia.domain.ParameterVO.ParameterEnum.NUMBER_OF_IMPORTED_CATALOGUE_ITEMS;
import static cz.ryvo.natia.domain.ParameterVO.ParameterEnum.TIME_OF_CATALOGUE_IMPORT;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Transactional(readOnly = true)
public class CatalogueServiceImpl implements CatalogueService {

    @Autowired
    private CatalogueReader catalogueReader;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ParameterService parameterService;

    @Override
    @Transactional
    public int importCatalogue(@NonNull MultipartFile file) {
        List<ArticleVO> articles;
        try {
            articles = catalogueReader.readCatalogue(file.getInputStream());
            if (isEmpty(articles)) {
                throw new Exception("Empty catalogue, no articles found. Cancelling operation.");
            }
        } catch (Exception e) {
            throw new BadRequestException(INVALID_FILE.class);
        }

        articles.forEach(articleRepository::save);

        parameterService.setParameter(TIME_OF_CATALOGUE_IMPORT, LocalDateTime.now().toString());
        parameterService.setParameter(FILENAME_OF_IMPORTED_CATALOGUE, file.getOriginalFilename());
        parameterService.setParameter(NUMBER_OF_IMPORTED_CATALOGUE_ITEMS, Long.toString(articles.size()));

        return articles.size();
    }

    @Override
    public List<ArticleVO> searchArticles(@NonNull String pattern) {
        if (pattern.length() > 3) {
            return articleRepository.findManyByPattern(pattern);
        } else {
            throw new BadRequestException(SEARCH_PARAMS_TOO_INACCURATE.class);
        }
    }

    @Override
    public ArticleVO getArticleByCode(@NonNull String code) {
        return articleRepository.findOne(code);
    }

    @Override
    public Map<ParameterEnum, String> getCatalogueInfo() {
        Set<ParameterEnum> ids = new HashSet<>(3);
        ids.add(TIME_OF_CATALOGUE_IMPORT);
        ids.add(FILENAME_OF_IMPORTED_CATALOGUE);
        ids.add(NUMBER_OF_IMPORTED_CATALOGUE_ITEMS);
        return parameterService.getParameters(ids);
    }
}
