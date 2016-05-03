package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.api.Catalogue;
import cz.ryvo.natia.converter.CatalogueConverter;
import cz.ryvo.natia.error.Errors.INVALID_FILE;
import cz.ryvo.natia.exception.BadRequestException;
import cz.ryvo.natia.service.CatalogueService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

    @RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Create catalogue by uploading a file", consumes = MULTIPART_FORM_DATA_VALUE)
    public Catalogue createCatalogue(@RequestPart("catalogue") Catalogue catalogue, @RequestPart("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                String content = IOUtils.toString(reader);
                System.out.println(content);
            }
        } catch (IOException e) {
            throw new BadRequestException(INVALID_FILE.class);
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Catalogue getCatalogue(@PathVariable("id") Long id) {
        return catalogueConverter.toApi(catalogueService.getCatalogue(id));
    }
}