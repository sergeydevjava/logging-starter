package com.sergeydevjava.aspect;

import com.sergeydevjava.annotation.LogExecutionTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Aspect
public class LogExecutionAspect {

    private static Logger log = LoggerFactory.getLogger(LogExecutionAspect.class);

    @Around("@annotation(com.sergeydevjava.annotation.LogExecutionTime)")
    public Object aroundLogExecutionTimeMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Instant start = Instant.now();

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LogExecutionTime originalAnnotation = method.getAnnotation(LogExecutionTime.class);
        Optional.ofNullable(originalAnnotation)
                .map(LogExecutionTime::methodName)
                .filter(StringUtils::hasText)
                .orElse(method.getName());


        try {
            return proceedingJoinPoint.proceed();
        } finally {
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toNanos();
            log.info("Время выполнения метода {}: {} мс", method.getName(), timeElapsed);
        }
    }

}
