//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String EMPTY = "";

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    public static boolean isAlphaNumeric(String str) {
        Pattern p = Pattern.compile("[0-9_a-zA-Z]*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String replaceSingleQuote(String str) {
        return str == null ? "" : trimToEmpty(str).replaceAll("'", "\\\\'");
    }

    public static String replaceBackSlash(String str) {
        return str == null ? "" : trimToEmpty(str).replace("\\", "\\\\");
    }

    public static String byteToString(Object byteObject) {
        return byteObject instanceof byte[] ? trimToEmpty(new String((byte[])byteObject)) : "";
    }

    public static String subString(String str, int length) {
        return str.length() < length ? str : str.substring(0, length);
    }
}
