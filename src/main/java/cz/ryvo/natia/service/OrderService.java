package cz.ryvo.natia.service;

import cz.ryvo.natia.domain.GiftArticleVO;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.util.List;

public interface OrderService {
    List<GiftArticleVO> processOrder(@Nonnull MultipartFile file);
}
