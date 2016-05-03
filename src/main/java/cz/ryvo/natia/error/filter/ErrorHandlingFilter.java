package cz.ryvo.natia.error.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.ryvo.natia.api.Error;
import cz.ryvo.natia.exception.BaseException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ErrorHandlingFilter extends OncePerRequestFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(NON_EMPTY);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Application error ", e);
            response.setContentType(APPLICATION_JSON_VALUE);
            if (e instanceof BaseException) {
                BaseException baseException = (BaseException) e;
                Error error = getError(baseException);
                response.setStatus(baseException.getResponseStatus());
                OBJECT_MAPPER.writeValue(response.getWriter(), singletonList(error));
            }
        }
    }

    private Error getError(BaseException baseException) {
        return Error.builder()
                .code(baseException.getErrorCode())
                .desc(baseException.getMessage())
                .build();
    }
}
