package com.sergeydevjava;

public class LoggingStarterAutoConfiguration {
    public static void println(String message) {
        System.out.println("Вызов из logging-starter " + message);
    }
}