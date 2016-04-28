package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.api.Catalogue;
import cz.ryvo.natia.converter.CatalogueConverter;
import cz.ryvo.natia.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = CatalogueEndpoint.ENDPOINT_URL_PATH)
public class CatalogueEndpoint {

    public static final String ENDPOINT_URL_PATH = "/api/v1/catalogues";

    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private CatalogueConverter catalogueConverter;

    @RequestMapping(method = GET)
    public List<Catalogue> listCatalogues() {
        return catalogueConverter.toApi(catalogueService.listCatalogues());
    }
}