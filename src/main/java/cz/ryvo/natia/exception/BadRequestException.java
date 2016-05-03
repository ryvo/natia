package cz.ryvo.natia.exception;

import cz.ryvo.natia.error.ErrorCode;

public class BadRequestException extends BaseException {

    public BadRequestException(Class<? extends ErrorCode> errorCodeClass) {
        super(errorCodeClass);
    }
}
