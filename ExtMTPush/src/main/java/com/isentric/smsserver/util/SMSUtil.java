package com.isentric.smsserver.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Component
public class SMSUtil {

    private static final Logger logger = LoggerFactory.getLogger(SMSUtil.class);

    private final DBUtil dbUtil;

    @Autowired
    public SMSUtil(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    /**
     * Convert text to GSM 7-bit encoding
     */
    public static String convertToGSM7Bit(String text) {
        if (text == null) {
            return null;
        }
        return text;
    }

    /**
     * Convert text to UCS2 (Unicode) encoding
     */
    public static String convertToUCS2(String text) {
        if (text == null) {
            return null;
        }
        try {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_16BE);
            return Hex.encodeHexString(bytes).toUpperCase();
        } catch (Exception e) {
            logger.error("Error converting to UCS2: {}", e.getMessage());
            return text;
        }
    }

    /**
     * Decode UCS2 encoded text
     */
    public static String decodeUCS2(String hexString) {
        if (hexString == null) {
            return null;
        }
        try {
            byte[] bytes = Hex.decodeHex(hexString.toCharArray());
            return new String(bytes, StandardCharsets.UTF_16BE);
        } catch (Exception e) {
            logger.error("Error decoding UCS2: {}", e.getMessage());
            return hexString;
        }
    }

    /**
     * Check if message contains non-GSM characters
     */
    public static boolean containsNonGSMCharacters(String text) {
        if (text == null) {
            return false;
        }
        String gsm7BitChars = "@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà";
        for (char c : text.toCharArray()) {
            if (gsm7BitChars.indexOf(c) == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate number of SMS parts
     */
    public static int calculateSMSParts(String message) {
        if (message == null || message.isEmpty()) {
            return 0;
        }

        boolean isUnicode = containsNonGSMCharacters(message);
        int length = message.length();

        if (isUnicode) {
            // UCS2 encoding: 70 chars single, 67 chars per concatenated part
            if (length <= 70) {
                return 1;
            }
            return (int) Math.ceil((double) length / 67);
        } else {
            // GSM 7-bit: 160 chars single, 153 chars per concatenated part
            if (length <= 160) {
                return 1;
            }
            return (int) Math.ceil((double) length / 153);
        }
    }

    /**
     * Format MSISDN to standard format
     */
    public static String formatMSISDN(String msisdn) {
        if (msisdn == null) {
            return null;
        }
        msisdn = msisdn.trim();

        // Remove leading + if present
        if (msisdn.startsWith("+")) {
            msisdn = msisdn.substring(1);
        }

        // Remove leading 00 if present
        if (msisdn.startsWith("00")) {
            msisdn = msisdn.substring(2);
        }

        return msisdn;
    }

    /**
     * Validate MSISDN format
     */
    public static boolean isValidMSISDN(String msisdn) {
        if (msisdn == null || msisdn.isEmpty()) {
            return false;
        }
        String formatted = formatMSISDN(msisdn);
        return formatted != null && formatted.matches("\\d{10,15}");
    }

    /**
     * Get data encoding type
     */
    public static int getDataEncoding(String message) {
        if (containsNonGSMCharacters(message)) {
            return 8; // UCS2
        }
        return 0; // GSM 7-bit
    }

    /**
     * Convert byte array to hex string
     */
    public static String bytesToHex(byte[] bytes) {
        return Hex.encodeHexString(bytes).toUpperCase();
    }

    /**
     * Convert hex string to byte array
     */
    public static byte[] hexToBytes(String hex) {
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (Exception e) {
            logger.error("Error converting hex to bytes: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Remove special characters from string (from original removeSpecialChars)
     */
    public static String removeSpecialChars(String inText) {
        if (inText == null) return "";

        char[] junkChar = new char[]{'\"', '\''};
        StringBuilder strB = new StringBuilder(inText);

        for (char c : junkChar) {
            int i = 0;
            while (i < strB.length()) {
                if (strB.charAt(i) == c) {
                    strB.deleteCharAt(i);
                    i--;
                }
                i++;
            }
        }
        return strB.toString();
    }

    /**
     * Get telco/operator from MSISDN (from original getTelco)
     */
    public static String getTelco(String rmsisdn) {
        if (rmsisdn == null || rmsisdn.length() < 5) {
            return "UNKNOWN";
        }

        // Format MSISDN first
        if (rmsisdn.startsWith("+")) {
            rmsisdn = rmsisdn.substring(1);
        }
        if (!rmsisdn.startsWith("60") && rmsisdn.startsWith("0")) {
            rmsisdn = "6" + rmsisdn;
        }

        // Check prefix for Malaysian telcos
        if (rmsisdn.startsWith("6012") || rmsisdn.startsWith("6017")) {
            return "6012"; // Maxis
        } else if (rmsisdn.startsWith("6016") || rmsisdn.startsWith("60146")) {
            return "6016"; // Digi
        } else if (rmsisdn.startsWith("6019") || rmsisdn.startsWith("6013") ||
                   rmsisdn.startsWith("6010") || rmsisdn.startsWith("6011")) {
            return "6019"; // Celcom
        } else if (rmsisdn.startsWith("6018")) {
            return "6018"; // U Mobile
        }
        return "UNKNOWN";
    }

    /**
     * Get today's date in specified format (from original getTodayDate)
     */
    public static String getTodayDate(String dateFormat) {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(dateFormat);
        return formatter.format(new java.util.Date());
    }

    /**
     * Convert URL and message to hex for WAP push (from original toHex)
     */
    public static String toHex(String url, String msg) {
        String startStr = "02056A0045C6080C03";
        String secondStr = "000AC3072004033014082310C30720050330140823010320";
        String lastStr = "20000101";

        StringBuilder result = new StringBuilder(startStr);
        for (char c : url.toCharArray()) {
            result.append(String.format("%02X", (int) c));
        }
        result.append(secondStr);
        for (char c : msg.toCharArray()) {
            result.append(String.format("%02X", (int) c));
        }
        result.append(lastStr);
        return result.toString();
    }

    /**
     * Set default parameters for MT message (from original setDefaultParamsMT)
     */
    public static void setDefaultParamsMT(com.isentric.smsserver.object.ExtSMSObject extSMS,
                                          String billFlag, String shortcode, String custid,
                                          String rmsisdn, String smsisdn, String mtid,
                                          String mtprice, int productType, String productCode,
                                          String keyword, int dataEncoding, String dataStr,
                                          String dataUrl, int dnrep, String groupTag,
                                          String urlTitle, String ewigFlag, String cFlag) {
        extSMS.setBillFlag(billFlag);
        extSMS.setShortcode(shortcode);
        extSMS.setCustid(custid);
        extSMS.setRmsisdn(rmsisdn);
        extSMS.setSmsisdn(smsisdn);
        extSMS.setMtid(mtid);
        extSMS.setMtprice(mtprice);
        extSMS.setProductType(productType);
        extSMS.setProductCode(productCode);
        extSMS.setKeyword(keyword);
        extSMS.setDataEncoding(dataEncoding);
        extSMS.setDataStr(dataStr);
        extSMS.setDataUrl(dataUrl);
        extSMS.setDnRep(dnrep);
        extSMS.setGroupTag(groupTag);
        extSMS.setUrlTitle(urlTitle);
        extSMS.setEwigFlag(ewigFlag);
        extSMS.setCFlag(cFlag);
    }

    /**
     * Reformat special characters for SQL (from original reformatSpecialChars)
     */
    public static String reformatSpecialChars(String inText) {
        if (inText == null) return "";
        String[] searchString = new String[]{"\\\\"};
        String[] formatString = new String[]{"\\\\\\\\"};
        String tempText = inText;
        for (int i = 0; i < searchString.length; i++) {
            tempText = tempText.replaceAll(searchString[i], formatString[i]);
        }
        return tempText;
    }
}

