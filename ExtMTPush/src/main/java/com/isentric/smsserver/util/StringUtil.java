package com.isentric.smsserver.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    public static String defaultString(String str, String defaultStr) {
        return StringUtils.defaultString(str, defaultStr);
    }

    public static String padLeft(String str, int size, char padChar) {
        return StringUtils.leftPad(str, size, padChar);
    }

    public static String padRight(String str, int size, char padChar) {
        return StringUtils.rightPad(str, size, padChar);
    }

    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    public static String replace(String text, String searchString, String replacement) {
        return StringUtils.replace(text, searchString, replacement);
    }

    public static String[] split(String str, String separator) {
        return StringUtils.split(str, separator);
    }

    public static String join(Object[] array, String separator) {
        return StringUtils.join(array, separator);
    }

    public static String toUpperCase(String str) {
        return StringUtils.upperCase(str);
    }

    public static String toLowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int twoHalfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = b & 0x0F;
            } while (twoHalfs++ < 1);
        }
        return buf.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}

