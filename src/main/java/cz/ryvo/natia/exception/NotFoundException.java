package cz.ryvo.natia.exception;

import cz.ryvo.natia.error.ErrorCode;

public class NotFoundException extends BaseException {
    public NotFoundException(Class<? extends ErrorCode> errorCodeClass) {
        super(errorCodeClass);
    }
}
