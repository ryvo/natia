package cz.ryvo.natia.service;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;

public interface OrderService {
    byte[] processOrder(@Nonnull MultipartFile file);

    String getFileExtension();
}
