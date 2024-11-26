package com.sergeydevjava.service;

import com.sergeydevjava.property.LoggingProperties;


public class WebContentManager {

    private final LoggingProperties loggingProperties;

    public WebContentManager(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    public boolean shouldBeExcluded(String request) {
        return loggingProperties.getExclude().stream()
                .anyMatch(request::matches);
    }

    public boolean shouldBeObfuscated(String headerName) {
        return loggingProperties.getObfuscate().getHeaders().stream()
                .anyMatch(headerName::matches);
    }
}
