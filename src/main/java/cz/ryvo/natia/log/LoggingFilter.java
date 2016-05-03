package cz.ryvo.natia.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;

public class LoggingFilter extends OncePerRequestFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Logger log;

    private final int maxContentSize;

    private final Set<String> excludedPaths;

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public LoggingFilter() {
        this(Builder.create());
    }

    public LoggingFilter(Builder builder) {
        requireNonNull(builder, "builder must not be null");

        this.maxContentSize = builder.maxContentSize;
        this.excludedPaths = builder.excludedPaths;
        this.log = builder.logger;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!log.isDebugEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        for (String excludedPath : excludedPaths) {
            String requestURI = request.getRequestURI();
            if (requestURI.startsWith(excludedPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        LoggingHttpServletRequestWrapper requestWrapper = new LoggingHttpServletRequestWrapper(request);
        LoggingHttpServletResponseWrapper responseWrapper = new LoggingHttpServletResponseWrapper(response);

        log.debug("REQUEST: " + getRequestDescription(requestWrapper));
        filterChain.doFilter(requestWrapper, responseWrapper);
        log.debug("RESPONSE: " + getResponseDescription(responseWrapper));
        response.getOutputStream().write(responseWrapper.getContentAsBytes());
    }

    private String getRequestDescription(LoggingHttpServletRequestWrapper requestWrapper) {
        LoggingRequest loggingRequest = new LoggingRequest();
        loggingRequest.setMethod(requestWrapper.getMethod());
        loggingRequest.setPath(requestWrapper.getRequestURI());
        loggingRequest.setParams(requestWrapper.isFormPost() ? null : requestWrapper.getParameters());
        loggingRequest.setHeaders(requestWrapper.getHeaders());
        String content = requestWrapper.getContent();
        if (log.isTraceEnabled()) {
            loggingRequest.setBody(content);
        } else {
            loggingRequest.setBody(content.substring(0, Math.min(content.length(), maxContentSize)));
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(loggingRequest);
        } catch (JsonProcessingException e) {
            log.warn("Cannot serialize Request to JSON", e);
            return null;
        }
    }

    private String getResponseDescription(LoggingHttpServletResponseWrapper responseWrapper) {
        LoggingResponse loggingResponse = new LoggingResponse();
        loggingResponse.setStatus(responseWrapper.getStatus());
        loggingResponse.setHeaders(responseWrapper.getHeaders());
        String content = responseWrapper.getContent();
        if (log.isTraceEnabled()) {
            loggingResponse.setBody(content);
        } else {
            loggingResponse.setBody(content.substring(0, Math.min(content.length(), maxContentSize)));
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(loggingResponse);
        } catch (JsonProcessingException e) {
            log.warn("Cannot serialize Response to JSON", e);
            return null;
        }
    }

    public int getMaxContentSize() {
        return maxContentSize;
    }

    public static class Builder {

        private int maxContentSize = 1024;

        private Set<String> excludedPaths = emptySet();

        private Logger logger = getLogger(LoggingFilter.class);

        public static Builder create() {
            return new Builder();
        }

        public Builder maxContentSize(int maxContentSize) {
            this.maxContentSize = maxContentSize;
            return this;
        }

        public Builder excludedPaths(@Nonnull String... excludedPaths) {
            requireNonNull(excludedPaths, "excludedPaths must not be null");
            this.excludedPaths = Stream.of(excludedPaths).collect(toSet());
            return this;
        }

        public Builder logger(@Nonnull Logger logger) {
            requireNonNull(logger, "logger must not be null");
            this.logger = logger;
            return this;
        }
    }
}
