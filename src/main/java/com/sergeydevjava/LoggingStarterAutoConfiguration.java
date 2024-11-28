package com.sergeydevjava;

import com.sergeydevjava.aspect.LogExecutionAspect;
import com.sergeydevjava.property.LoggingProperties;
import com.sergeydevjava.service.WebContentManager;
import com.sergeydevjava.webfilter.WebLoggingFilter;
import com.sergeydevjava.webfilter.WebLoggingRequestBodyAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "logging", value = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingStarterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "logging", value = "log-exec-time", havingValue = "true")
    public LogExecutionAspect logExecutionAspect() {
        return new LogExecutionAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = "enabled", havingValue = "true", matchIfMissing = true)
    public WebLoggingFilter webLoggingFilter(WebContentManager webContentManager) {
        return new WebLoggingFilter(webContentManager);
    }

    @Bean
    @ConditionalOnBean(WebLoggingFilter.class)
    @ConditionalOnProperty(prefix = "logging.web-logging", value = "log-body", havingValue = "true")
    public WebLoggingRequestBodyAdvice webLoggingRequestBodyAdvice(WebContentManager webContentManager) {
        return new WebLoggingRequestBodyAdvice(webContentManager);
    }

    @Bean
    @ConditionalOnBean(WebLoggingFilter.class)
    public WebContentManager webContentManager(LoggingProperties loggingProperties) {
        return new WebContentManager(loggingProperties);
    }

}