package cz.ryvo.natia.error;

import cz.ryvo.natia.api.Error;
import cz.ryvo.natia.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.springframework.util.CollectionUtils.isEmpty;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

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
