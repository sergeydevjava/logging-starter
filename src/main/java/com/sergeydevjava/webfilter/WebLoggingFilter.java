package com.sergeydevjava.webfilter;

import com.sergeydevjava.service.WebContentManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class WebLoggingFilter extends HttpFilter {

    private final WebContentManager webContentManager;

    private static final Logger log = LoggerFactory.getLogger(WebLoggingFilter.class);

    public WebLoggingFilter(WebContentManager webContentManager) {
        this.webContentManager = webContentManager;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI() + formatQueryString(request);

        if (webContentManager.shouldBeExcluded(request)) {
            super.doFilter(request, response, chain);
        } else {
            doFilterWithLogging(request, response, chain, method, requestURI);
        }
    }

    private void doFilterWithLogging(HttpServletRequest request, HttpServletResponse response, FilterChain chain, String method, String requestURI) throws IOException, ServletException {
        String headers = inlineHeaders(request);
        log.info("Запрос {} {} {}", method, requestURI, headers);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {

            super.doFilter(request, responseWrapper, chain);

            String responseBody = "body=" + new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info("Ответ: {} {} {} {}", method, requestURI, response.getStatus(), responseBody);
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }

    private String inlineHeaders(HttpServletRequest request) {
        Map<String, String> headersMap = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(it -> it, request::getHeader));
        String inlineHeaders = headersMap.entrySet().stream()
                .map(entry -> {
                    String headerName = entry.getKey();
                    String headerValue = webContentManager.shouldBeObfuscated(headerName)
                            ? "*********"
                            : entry.getValue();
                    return headerName + "=" + headerValue;
                })
                .collect(Collectors.joining(","));
        return "headers={" + inlineHeaders + "}";
    }

    private String formatQueryString(HttpServletRequest request) {
        return Optional.ofNullable(request.getQueryString())
                .map(qs -> "?=" + qs)
                .orElse(Strings.EMPTY);
    }

}
