package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.api.CreateResult;
import cz.ryvo.natia.exception.InternalServerException;
import cz.ryvo.natia.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.nanoTime;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = OrderEndpoint.ENDPOINT_URL_PATH)
public class OrderEndpoint {

    public static final String ENDPOINT_URL_PATH = "/api/v1/orders";

    private static final String PROCESSED_ORDER_TMP_FILE_PREFIX = "natia_processed_order_";

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Process order by uploading a file", consumes = MULTIPART_FORM_DATA_VALUE)
    public CreateResult processOrder(MultipartFile file) {
        byte[] data = orderService.processOrder(file);
        Long id = nanoTime();
        try {
            Path tempFile = Paths.get(System.getProperty("java.io.tmpdir"), PROCESSED_ORDER_TMP_FILE_PREFIX + Long.toString(id) + orderService.getFileExtension());
            try (OutputStream out = Files.newOutputStream(tempFile)) {
                out.write(data);
            }
        } catch (IOException e) {
            throw new InternalServerException(e);
        }

        return new CreateResult(id);
    }

    @RequestMapping(path = "/{id}", method = GET)
    @ApiOperation(value = "Returns processed order as a file download")
    public void downloadProcessedOrder(@PathVariable("id") Long id, HttpServletResponse response) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(tmpDir, PROCESSED_ORDER_TMP_FILE_PREFIX + Long.toString(id) + orderService.getFileExtension());
        try {
            InputStream is = Files.newInputStream(path);
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(path.getFileName().toString(), "UTF-8"));
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
