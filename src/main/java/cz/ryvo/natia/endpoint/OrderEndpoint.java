package cz.ryvo.natia.endpoint;

import cz.ryvo.natia.api.GiftArticle;
import cz.ryvo.natia.converter.GiftArticleConverter;
import cz.ryvo.natia.domain.GiftArticleVO;
import cz.ryvo.natia.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = OrderEndpoint.ENDPOINT_URL_PATH)
public class OrderEndpoint {

    public static final String ENDPOINT_URL_PATH = "/api/v1/orders";

    private static final String PROCESSED_ORDER_TMP_FILE_PREFIX = "natia_processed_order_";

    @Autowired
    private OrderService orderService;

    @Autowired
    private GiftArticleConverter giftArticleConverter;

    @RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Process order by uploading a file", consumes = MULTIPART_FORM_DATA_VALUE)
    public List<GiftArticle> processOrder(MultipartFile file) {
        List<GiftArticleVO> giftArticles = orderService.processOrder(file);
        return giftArticleConverter.toApi(giftArticles);
    }
}
