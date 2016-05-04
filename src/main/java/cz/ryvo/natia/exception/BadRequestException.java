package cz.ryvo.natia.exception;

import cz.ryvo.natia.error.ErrorCode;

import javax.servlet.http.HttpServletResponse;

public class BadRequestException extends BaseException {

    public BadRequestException(Class<? extends ErrorCode> errorCodeClass) {
        super(errorCodeClass);
    }

    public int getResponseStatus() {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
}
