package com.isentric.smsserver.util;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Logger Factory utility for creating loggers.
 * This is a wrapper around SLF4J LoggerFactory for consistency with legacy code.
 */
@Component
public class LoggerUtil {

    public static Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(String name) {
        return org.slf4j.LoggerFactory.getLogger(name);
    }

    public static void info(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void info(Logger logger, String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    public static void debug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void debug(Logger logger, String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    public static void error(Logger logger, String message) {
        logger.error(message);
    }

    public static void error(Logger logger, String message, Throwable t) {
        logger.error(message, t);
    }

    public static void error(Logger logger, String message, Object... args) {
        logger.error(message, args);
    }

    public static void warn(Logger logger, String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public static void warn(Logger logger, String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }
}

