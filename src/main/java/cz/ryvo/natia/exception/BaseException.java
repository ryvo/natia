package cz.ryvo.natia.exception;

import cz.ryvo.natia.error.ErrorCode;
import cz.ryvo.natia.error.ErrorContextHolder;

import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class BaseException extends RuntimeException {

    private final Class<? extends ErrorCode> errorCodeClass;

    private int responseStatus = SC_INTERNAL_SERVER_ERROR;

    private Map<String, Object> params;

    public BaseException(Class<? extends ErrorCode> errorCodeClass) {
        this.errorCodeClass = errorCodeClass;
    }

    public BaseException(Class<? extends ErrorCode> errorCodeClass, Map<String, Object> params) {
        this.errorCodeClass = errorCodeClass;
        this.params = params;
    }

    public BaseException(Class<? extends ErrorCode> errorCodeClass, Object... params) {
        this.errorCodeClass = errorCodeClass;
        this.params = ErrorContextHolder.createParamsMap(ErrorContextHolder.getRawMessage(errorCodeClass), params);
    }

    public String getErrorCode() {
        return ErrorContextHolder.getCode(errorCodeClass);
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getRawDesc() {
        return ErrorContextHolder.getRawMessage(errorCodeClass);
    }
}
