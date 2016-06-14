package cz.ryvo.natia.error;

import cz.ryvo.natia.api.Error;
import cz.ryvo.natia.exception.BaseException;
import cz.ryvo.natia.exception.InternalServerException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.util.CollectionUtils.isEmpty;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<Error>> handleException(Exception exception) {
        Throwable cause;
        if (exception instanceof BaseException) {
            cause = exception;
        } else {
            cause = ExceptionUtils.getRootCause(exception);
            if (cause == null) {
                cause = exception;
            }
        }

        if (cause instanceof BaseException) {
            BaseException baseException = (BaseException) cause;
            List<Error> errors = processException(baseException);
            return new ResponseEntity<>(errors, valueOf(baseException.getResponseStatus()));
        }

        List<Error> errors = processException(new InternalServerException(cause));
        return new ResponseEntity<>(errors, INTERNAL_SERVER_ERROR);
    }

    private List<Error> processException(BaseException exception) {
        return singletonList(createError(exception));
    }

    private Error createError(BaseException exception) {
        log.info("Controller method returned error: {}", exception.getMessage());
        log.debug("Exception stack trace", exception);

        Error error = new Error();
        error.setCode(exception.getErrorCode());
        Map<String, Object> params = exception.getParams();
        error.setArgs(params != null ? params : emptyMap());
        error.setDesc(exception.getMessage());
        error.setRawDesc(isEmpty(params) ? null : exception.getRawDesc());
        return error;
    }
}
