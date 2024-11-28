package com.sergeydevjava.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "web-logging")
public class LoggingProperties {

    private final List<AntPathRequestMatcher> exclude = new ArrayList<>();
    private final Obfuscate obfuscate = new Obfuscate();

    public List<AntPathRequestMatcher> getExclude() {
        return exclude;
    }

    public Obfuscate getObfuscate() {
        return obfuscate;
    }

    public static class Obfuscate {

        private final List<String> headers = new ArrayList<>();

        public List<String> getHeaders() {
            return headers;
        }
    }
}
