//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.manager;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.ConsoleAppender;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class LoggerManager {
    private static final String DEFAULT_LOG_PATH_PREFIX = System.getProperty("user.home") + "/logs/bulk-gateway";
    private static final int DEFAULT_MAX_BACKUP_INDEX = 10;
    private static final Hashtable<String, Logger> loggerHashtable = new Hashtable();

    public static Logger createLoggerPattern(Class c) {
        // Use a safe default location under the current user's home directory.
        return createLoggerPattern(c, DEFAULT_LOG_PATH_PREFIX, DEFAULT_MAX_BACKUP_INDEX);
    }

    public static Logger createLoggerPattern(Class c, int maxBackupIndex) {
        return createLoggerPattern(c, DEFAULT_LOG_PATH_PREFIX, maxBackupIndex);
    }

    public static Logger createLoggerPattern(Class c, String pathprefix, int maxBackupIndex) {
         Logger logger = Logger.getLogger(c);
         String pattern = ">>  %d{ISO8601} # %l # %m%n";
        // Ensure pathprefix does not end with a slash (we'll add separators consistently)
        String basePrefix = (pathprefix == null || pathprefix.isEmpty()) ? DEFAULT_LOG_PATH_PREFIX : pathprefix;
        String dirPath = basePrefix + File.separator + getDateToFormattedString(new Date(), "yyyyMMdd") + File.separator;
        // Use the class simple name as the log filename to make files discoverable
        String outPath = dirPath + c.getSimpleName() + ".log";
         System.out.println(outPath);
         boolean dirOk = createLoggerFilePath(dirPath);
         PatternLayout layout = new PatternLayout(pattern);
         if (dirOk) {
             try {
                 RollingFileAppender appender = new RollingFileAppender(layout, outPath);
                 appender.setMaximumFileSize(appender.getMaximumFileSize() * 2L);
                 appender.setMaxBackupIndex(maxBackupIndex);
                 logger.addAppender(appender);
                 logger.setLevel(Level.ALL);
                 return logger;
             } catch (IOException ioe) {
                // If file appender creation fails, print a helpful message and fall back to console
                System.err.println("LoggerManager: failed to create file appender for path=" + outPath + ", falling back to console: " + ioe.getMessage());
             }
         }

         // Fallback: console appender to avoid application startup failure when file cannot be created
         ConsoleAppender console = new ConsoleAppender(layout);
         logger.addAppender(console);
         logger.setLevel(Level.ALL);

         return logger;
     }

    public static Logger createLoggerPattern(String loggerName, String loggerFileName) {
        Logger logger = Logger.getLogger(loggerName);
        String pattern = ">>  %d{ISO8601} # %l # %m%n";
        String dirPath = "/home/arun/Desktop/log/" + getDateToFormattedString(new Date(), "yyyyMMdd") + "/";
        String outPath = dirPath + loggerFileName + ".txt";
        boolean dirOk = createLoggerFilePath(dirPath);
        PatternLayout layout = new PatternLayout(pattern);
        if (dirOk) {
            try {
                RollingFileAppender appender = new RollingFileAppender(layout, outPath);
                appender.setMaximumFileSize(appender.getMaximumFileSize() * 2L);
                appender.setMaxBackupIndex(10);
                logger.addAppender(appender);
                logger.setLevel(Level.ALL);
                return logger;
            } catch (IOException ioe) {
                // fall through
            }
        }

        ConsoleAppender console = new ConsoleAppender(layout);
        logger.addAppender(console);
        logger.setLevel(Level.ALL);

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
        String dirPath = "/home/arun/Desktop/log/" + getDateToFormattedString(new Date(), "yyyyMMdd") + "/";
        String outPath = dirPath + loggerFileName + ".sql";
        boolean dirOk = createLoggerFilePath(dirPath);
        PatternLayout layout = new PatternLayout(pattern);
        if (dirOk) {
            try {
                RollingFileAppender appender = new RollingFileAppender(layout, outPath);
                appender.setMaximumFileSize(appender.getMaximumFileSize() * 2L);
                appender.setMaxBackupIndex(10);
                logger.addAppender(appender);
                logger.setLevel(Level.ALL);
                return logger;
            } catch (IOException ioe) {
                // fall through
            }
        }

        ConsoleAppender console = new ConsoleAppender(layout);
        logger.addAppender(console);
        logger.setLevel(Level.ALL);

        return logger;
    }

    private static String getDateToFormattedString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String formattedString = formatter.format(date);
        return formattedString;
    }

    private static synchronized boolean createLoggerFilePath(String dirPath) {
        try {
            File d = new File(dirPath);
            if (!d.exists()) {
                return d.mkdirs();
            }
            return true;
        } catch (SecurityException se) {
            se.printStackTrace();
            return false;
        }
    }
}
