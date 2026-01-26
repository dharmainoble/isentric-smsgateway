//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.model.SMSMessageSmpp;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;

public class ContentUtil {
    private static final Logger logger = LoggerManager.createLoggerPattern(ContentUtil.class);

    public static ArrayList reformatContent(SMSMessageSmpp smsMessage) {
        ArrayList message = new ArrayList();
        int nMessages = 0;
        int contentPartNumber = 0;
        String contentPart = "";
        String contentHeader = "";
        String content = smsMessage.getMessage();
        if (smsMessage.getMessageType() == 5) {
            nMessages = (int)Math.ceil((double)smsMessage.getMessage().length() / (double)256.0F);
            contentPartNumber = 256;
        } else if (smsMessage.getMessageType() == 4) {
            nMessages = (int)Math.ceil((double)smsMessage.getMessage().length() / (double)256.0F);
            contentPartNumber = 256;
        } else if (smsMessage.getMessageType() == 3) {
            nMessages = (int)Math.ceil((double)smsMessage.getMessage().length() / (double)256.0F);
            contentPartNumber = 256;
        } else if (smsMessage.getMessageType() == 1) {
            logger.info("URL >> " + smsMessage.getMessage());
            content = getToHex(smsMessage.getMessage(), "123");
            logger.info("Hex >> " + content);
            nMessages = (int)Math.ceil((double)smsMessage.getMessage().length() / (double)260.0F);
            contentPartNumber = 260;
        }

        for(int i = 0; i < nMessages; ++i) {
            contentHeader = getContentHeader(smsMessage.getMessageType(), i + 1, nMessages);
            contentPart = getContentPart(smsMessage.getMessageType(), content, i, nMessages, contentPartNumber);
            message.add(contentHeader.trim() + contentPart.trim());
            logger.info(contentHeader.trim() + contentPart.trim());
        }

        return message;
    }

    private static String getContentHeader(int contentType, int partNumber, int totalPart) {
        if (contentType == 4) {
            return getContentHeaderLogo(partNumber, totalPart);
        } else if (contentType == 5) {
            return getContentHeaderPicture(partNumber, totalPart);
        } else if (contentType == 3) {
            return getContentHeaderRingtone(partNumber, totalPart);
        } else if (contentType == 2) {
            return getContentHeaderOTA(partNumber, totalPart);
        } else {
            return contentType == 1 ? getContentHeaderWAP() : "";
        }
    }

    private static String getContentPart(int contentType, String content, int partNumber, int totalPart, int totalBytes) {
        return contentType == 1 ? getContentPartWAP(content, partNumber, totalPart) : getContentPart(content, partNumber, totalBytes);
    }

    public static String getContentHeaderRingtone(int partNumber, int totalNumber) {
        String udhLength = null;
        String initialHeader = "000300";
        String header = "";
        boolean includeUdhLength = true;
        if (totalNumber > 1) {
            udhLength = "0B";
            if (totalNumber < 10) {
                header = initialHeader + "0" + totalNumber;
            } else {
                header = initialHeader + String.valueOf(totalNumber);
            }

            if (partNumber < 10) {
                header = header + "0" + partNumber;
            } else {
                header = header + String.valueOf(partNumber);
            }
        } else {
            udhLength = "06";
        }

        String ringtoneHeader = "050415810000";
        if (includeUdhLength) {
            header = udhLength + header + ringtoneHeader;
        } else {
            header = header + ringtoneHeader;
        }

        return header;
    }

    public static String getContentHeaderPicture(int partNumber, int totalNumber) {
        String udhLength = null;
        String initialHeader = "000300";
        String header = "";
        boolean includeUdhLength = true;
        if (totalNumber > 1) {
            udhLength = "0B";
            if (totalNumber < 10) {
                header = initialHeader + "0" + totalNumber;
            } else {
                header = initialHeader + String.valueOf(totalNumber);
            }

            if (partNumber < 10) {
                header = header + "0" + partNumber;
            } else {
                header = header + String.valueOf(partNumber);
            }
        } else {
            udhLength = "06";
        }

        String pictureHeader = "0504158A0000";
        if (includeUdhLength) {
            header = udhLength + header + pictureHeader;
        } else {
            header = header + pictureHeader;
        }

        return header;
    }

    public static String getContentHeaderLogo(int partNumber, int totalNumber) {
        String udhLength = null;
        String initialHeader = "000300";
        String header = "";
        boolean includeUdhLength = true;
        if (totalNumber > 1) {
            udhLength = "0B";
            if (totalNumber < 10) {
                header = initialHeader + "0" + totalNumber;
            } else {
                header = initialHeader + String.valueOf(totalNumber);
            }

            if (partNumber < 10) {
                header = header + "0" + partNumber;
            } else {
                header = header + String.valueOf(partNumber);
            }
        } else {
            udhLength = "06";
        }

        String logoHeader = "050415820000";
        if (includeUdhLength) {
            header = udhLength + header + logoHeader;
        } else {
            header = header + logoHeader;
        }

        return header;
    }

    public static String getContentHeaderOTA(int partNumber, int totalNumber) {
        String udhLength = null;
        String initialHeader = "000372";
        String header = "";
        if (totalNumber > 1) {
            udhLength = "0B";
            if (totalNumber < 10) {
                header = initialHeader + "0" + totalNumber;
            } else {
                header = initialHeader + String.valueOf(totalNumber);
            }

            if (partNumber < 10) {
                header = header + "0" + partNumber;
            } else {
                header = header + String.valueOf(partNumber);
            }
        } else {
            udhLength = "06";
        }

        String pictureHeader = "0504C34FC002";
        header = udhLength + pictureHeader + header;
        return header;
    }

    public static String getContentHeaderUnicode(int partNumber, int totalNumber) {
        String udhLength = null;
        String initialHeader = "000300";
        String header = "";
        if (totalNumber > 1) {
            udhLength = "05";
            if (totalNumber < 10) {
                header = initialHeader + "0" + totalNumber;
            } else {
                header = initialHeader + String.valueOf(totalNumber);
            }

            if (partNumber < 10) {
                header = header + "0" + partNumber;
            } else {
                header = header + String.valueOf(partNumber);
            }
        } else {
            udhLength = "";
        }

        header = udhLength + header;
        return header;
    }

    public static String getContentPart(String content, int partNumber, int totalBytes) {
        String contentPart = null;
        int firstIndex = partNumber * totalBytes;
        int followIndex = (partNumber + 1) * totalBytes;
        int messageLength = content.length();
        if (followIndex < messageLength) {
            contentPart = content.substring(firstIndex, followIndex);
        } else {
            contentPart = content.substring(firstIndex, messageLength);
        }

        return contentPart;
    }

    public static String getContentHeaderWAP() {
        String header = "0605040B8423F0";
        return header;
    }

    public static String getContentHeaderWAP(int partNumber, int total) {
        String header = "0B0504C34F23F00003C20" + total + "0" + partNumber;
        return header;
    }

    public static String getContentPartWAP(String content, int partNumber, int total) {
        String contentPart = null;
        int firstIndex = partNumber * 260;
        int followIndex = (partNumber + 1) * 260;
        int messageLength = content.length();
        if (followIndex < messageLength) {
            contentPart = content.substring(firstIndex, followIndex);
        } else {
            contentPart = content.substring(firstIndex, messageLength);
        }

        return partNumber == 0 ? "350601AE" + contentPart : contentPart;
    }

    public static String toHex(String url, String msg) {
        String startStr = "02056A0045C6080C03";
        String secondStr = "000AC3072004033014082310C30720050330140823010320";
        String lastStr = "20000101";

        for(int i = 0; i < url.length(); ++i) {
            char c = url.charAt(i);
            startStr = startStr + Integer.toHexString(c).toUpperCase();
        }

        startStr = startStr + secondStr;

        for(int j = 0; j < msg.length(); ++j) {
            char c = msg.charAt(j);
            startStr = startStr + Integer.toHexString(c).toUpperCase();
        }

        startStr = startStr + lastStr;
        return startStr;
    }

    public static String getToHex(String url, String message) {
        Calendar cal = Calendar.getInstance();
        String out = "02056A0045C6080C03";
        String append = "";
        String appenddate = "000AC307";
        String appenddate2 = "02125610C307";
        String appenddate3 = "021256010320";
        String appendlast = "000101";
        int year = cal.get(1);
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        int nextyear = year + 1;

        for(int i = 0; i < url.length(); ++i) {
            char c = url.charAt(i);
            out = out + Integer.toHexString(c);
        }

        if (month < 10 && day < 10) {
            out = out + appenddate + Integer.toString(year) + "0" + Integer.toString(month) + "0" + Integer.toString(day);
            out = out + appenddate2 + Integer.toString(nextyear) + "0" + Integer.toString(month) + "0" + Integer.toString(day);
            out = out + appenddate3;
        } else if (month < 10) {
            out = out + appenddate + Integer.toString(year) + "0" + Integer.toString(month) + Integer.toString(day);
            out = out + appenddate2 + Integer.toString(nextyear) + "0" + Integer.toString(month) + Integer.toString(day);
            out = out + appenddate3;
        } else if (day < 10) {
            out = out + appenddate + Integer.toString(year) + Integer.toString(month) + "0" + Integer.toString(day);
            out = out + appenddate2 + Integer.toString(nextyear) + Integer.toString(month) + "0" + Integer.toString(day);
            out = out + appenddate3;
        } else {
            out = out + appenddate + Integer.toString(year) + Integer.toString(month) + Integer.toString(day);
            out = out + appenddate2 + Integer.toString(nextyear) + Integer.toString(month) + Integer.toString(day);
            out = out + appenddate3;
        }

        for(int x = 0; x < message.length(); ++x) {
            char c = message.charAt(x);
            out = out + Integer.toHexString(c);
        }

        out = out + appendlast;
        return out.toUpperCase();
    }
}
