package cz.ryvo.natia.exception;

import cz.ryvo.natia.error.Errors.RESOURCE_NOT_FOUND;

public class NotFoundException extends BaseException {

    public NotFoundException() {
        super(RESOURCE_NOT_FOUND.class);
    }

    public NotFoundException(Object... params) {
        super(RESOURCE_NOT_FOUND.class, params);
    }
}
