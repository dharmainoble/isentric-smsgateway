package com.isentric.smsserver.util;

import org.springframework.stereotype.Component;

@Component
public class SmsUtil {
    
    /**
     * Get telco prefix from MSISDN
     */
    public static String getTelco(String msisdn) {
        if (msisdn == null || msisdn.length() < 4) {
            return "";
        }
        
        // Normalize MSISDN
        String normalizedMsisdn = normalizeMsisdn(msisdn);
        
        // Check prefixes
        if (normalizedMsisdn.startsWith("6019") || normalizedMsisdn.startsWith("60148")) {
            return "CELCOM";
        } else if (normalizedMsisdn.startsWith("6016") || normalizedMsisdn.startsWith("60146") || normalizedMsisdn.startsWith("60143")) {
            return "DIGI";
        } else if (normalizedMsisdn.startsWith("6012") || normalizedMsisdn.startsWith("60142") || 
                   normalizedMsisdn.startsWith("6017") || normalizedMsisdn.startsWith("60147") || 
                   normalizedMsisdn.startsWith("6011")) {
            return "MAXIS";
        } else if (normalizedMsisdn.startsWith("6018") || normalizedMsisdn.startsWith("60183")) {
            return "UMOBILE";
        }
        
        return "UNKNOWN";
    }
    
    /**
     * Normalize MSISDN to international format
     */
    public static String normalizeMsisdn(String msisdn) {
        if (msisdn == null) {
            return "";
        }
        
        msisdn = msisdn.trim();
        
        // Remove leading + or 00
        if (msisdn.startsWith("+")) {
            msisdn = msisdn.substring(1);
        } else if (msisdn.startsWith("00")) {
            msisdn = msisdn.substring(2);
        }
        
        // Convert local format to international
        if ((msisdn.length() == 10 || msisdn.length() == 11) && msisdn.startsWith("01")) {
            msisdn = "6" + msisdn;
        }
        
        return msisdn;
    }
    
    /**
     * Remove special characters from string for SQL safety
     */
    public static String removeSpecialChars(String input) {
        if (input == null) {
            return "";
        }
        // Remove or escape single quotes for SQL
        return input.replace("'", "''")
                   .replace("\\", "\\\\");
    }
    
    /**
     * Check if MSISDN is local (Malaysian)
     */
    public static boolean isLocalMsisdn(String msisdn) {
        String normalized = normalizeMsisdn(msisdn);
        return normalized.startsWith("601");
    }
    
    /**
     * Validate MSISDN format
     */
    public static boolean isValidMsisdn(String msisdn) {
        if (msisdn == null || msisdn.isEmpty()) {
            return false;
        }
        
        String normalized = normalizeMsisdn(msisdn);
        
        // Malaysian number should be 11-12 digits starting with 60
        if (normalized.startsWith("60")) {
            return normalized.length() >= 11 && normalized.length() <= 13;
        }
        
        // International numbers
        return normalized.length() >= 10 && normalized.length() <= 15;
    }
}

