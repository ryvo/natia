package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.api.Article;
import cz.ryvo.natia.api.CatalogueImportResult;
import cz.ryvo.natia.api.CatalogueInfo;
import cz.ryvo.natia.converter.ArticleConverter;
import cz.ryvo.natia.converter.CatalogueInfoConverter;
import cz.ryvo.natia.service.CatalogueService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = CatalogueEndpoint.ENDPOINT_URL_PATH)
public class CatalogueEndpoint {

    public static final String ENDPOINT_URL_PATH = "/api/v1/catalogue";

    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private ArticleConverter articleConverter;

    @Autowired
    private CatalogueInfoConverter catalogueInfoConverter;

    @RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Create catalogue by uploading a file", consumes = MULTIPART_FORM_DATA_VALUE)
    public CatalogueImportResult importCatalogue(MultipartFile file) {
        return new CatalogueImportResult(catalogueService.importCatalogue(file));
    }

    @RequestMapping(path = "/articles/search", method = GET)
    @ApiOperation(value = "Search articles by code or description")
    public List<Article> searchArticles(@RequestParam("pattern") String pattern) {
        return articleConverter.toApi(catalogueService.searchArticles(pattern));
    }

    @RequestMapping(path = "/info", method = GET)
    @ApiOperation(value = "Get information about imported catalogue")
    public CatalogueInfo getCatalogueInfo() {
        return catalogueInfoConverter.toApi(catalogueService.getCatalogueInfo());
    }
}