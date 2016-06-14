package cz.ryvo.natia.exception;

import cz.ryvo.natia.error.Errors.INTERNAL_ERROR;

import static java.util.Collections.singletonMap;

public class InternalServerException extends BaseException {

    public InternalServerException(Throwable cause) {
        super(INTERNAL_ERROR.class, singletonMap("reason", cause.getMessage()), cause);
    }
}
