package cz.ryvo.natia.exception;

import cz.ryvo.natia.error.ErrorCode;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class BadRequestException extends BaseException {

    public BadRequestException(Class<? extends ErrorCode> errorCodeClass) {
        super(errorCodeClass);
    }

    public BadRequestException(Class<? extends ErrorCode> errorCodeClass, Map<String, Object> params) {
        super(errorCodeClass, params);
    }

    public BadRequestException(Class<? extends ErrorCode> errorCodeClass, Object... params) {
        super(errorCodeClass, params);
    }

    @Override
    public int getResponseStatus() {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
}
