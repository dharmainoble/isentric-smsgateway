//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.manager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class LoggerManager {
    private static final String DEFAULT_LOG_PATH_PREFIX = "/app/logs/bulk-gateway/";
    private static final int DEFAULT_MAX_BACKUP_INDEX = 10;
    private static final Hashtable<String, Logger> loggerHashtable = new Hashtable();

    public static Logger createLoggerPattern(Class c) {
        return createLoggerPattern(c, "/app/logs/bulk-gateway/", 10);
    }

    public static Logger createLoggerPattern(Class c, int maxBackupIndex) {
        return createLoggerPattern(c, "/app/logs/bulk-gateway/", maxBackupIndex);
    }

    public static Logger createLoggerPattern(Class c, String pathprefix, int maxBackupIndex) {
        Logger logger = Logger.getLogger(c);
        String pattern = ">>  %d{ISO8601} # %l # %m%n";
        String dirPath = pathprefix + "/" + getDateToFormattedString(new Date(), "yyyyMMdd") + "/";
        String outPath = dirPath + c.getName() + ".txt";
        createLoggerFilePath(dirPath);

        try {
            PatternLayout layout = new PatternLayout(pattern);
            RollingFileAppender appender = new RollingFileAppender(layout, outPath);
            appender.setMaximumFileSize(appender.getMaximumFileSize() * 2L);
            appender.setMaxBackupIndex(maxBackupIndex);
            logger.addAppender(appender);
            logger.setLevel(Level.ALL);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return logger;
    }

    public static Logger createLoggerPattern(String loggerName, String loggerFileName) {
        Logger logger = Logger.getLogger(loggerName);
        String pattern = ">>  %d{ISO8601} # %l # %m%n";
        String dirPath = "/app/logs/bulk-gateway//" + getDateToFormattedString(new Date(), "yyyyMMdd") + "/";
        String outPath = dirPath + loggerFileName + ".txt";
        createLoggerFilePath(dirPath);

        try {
            PatternLayout layout = new PatternLayout(pattern);
            RollingFileAppender appender = new RollingFileAppender(layout, outPath);
            appender.setMaximumFileSize(appender.getMaximumFileSize() * 2L);
            appender.setMaxBackupIndex(10);
            logger.addAppender(appender);
            logger.setLevel(Level.ALL);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return logger;
    }

    public static Logger createLoggerPatternSQL(String loggerName, String loggerFileName) {
        Logger logger = null;
        if (loggerHashtable.containsKey(loggerName)) {
            logger = (Logger)loggerHashtable.get(loggerName);
        } else {
            logger = Logger.getLogger(loggerName);
            loggerHashtable.put(loggerName, logger);
        }

        String pattern = "%m%n";
        String dirPath = "/app/logs/bulk-gateway//" + getDateToFormattedString(new Date(), "yyyyMMdd") + "/";
        String outPath = dirPath + loggerFileName + ".sql";
        createLoggerFilePath(dirPath);

        try {
            PatternLayout layout = new PatternLayout(pattern);
            RollingFileAppender appender = new RollingFileAppender(layout, outPath);
            appender.setMaximumFileSize(appender.getMaximumFileSize() * 2L);
            appender.setMaxBackupIndex(10);
            logger.addAppender(appender);
            logger.setLevel(Level.ALL);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return logger;
    }

    private static String getDateToFormattedString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String formattedString = formatter.format(date);
        return formattedString;
    }

    private static synchronized boolean createLoggerFilePath(String dirPath) {
        boolean dirsCreateSuccessFlag = (new File(dirPath)).mkdirs();
        return dirsCreateSuccessFlag;
    }
}
