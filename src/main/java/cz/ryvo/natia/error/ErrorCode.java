package cz.ryvo.natia.error;

import javax.validation.Payload;

public interface ErrorCode extends Payload {
    String UNKNOWN_ERROR_CODE = "1000";
    String UNKNOWN_ERROR_MESSAGE = "Unknown error";
}
