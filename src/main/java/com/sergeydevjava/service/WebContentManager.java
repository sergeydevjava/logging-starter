package com.sergeydevjava.service;

import com.sergeydevjava.property.LoggingProperties;
import jakarta.servlet.http.HttpServletRequest;


public class WebContentManager {

    private final LoggingProperties loggingProperties;

    public WebContentManager(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    public boolean shouldBeExcluded(HttpServletRequest request) {
        return loggingProperties.getExclude().stream()
                .anyMatch(antPathRequestMatcher -> antPathRequestMatcher.matches(request));
    }

    public boolean shouldBeObfuscated(String headerName) {
        return loggingProperties.getObfuscate().getHeaders().stream()
                .anyMatch(headerName::equalsIgnoreCase);
    }
}
