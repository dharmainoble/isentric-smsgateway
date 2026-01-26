//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.manager.LoggerManager;
import org.apache.log4j.Logger;

public class SmsUtil {
    private static final Logger logger = LoggerManager.createLoggerPattern(SmsUtil.class.getName(), "ExceptionLog");
    private static final Logger loggerBusiness = LoggerManager.createLoggerPattern(SmsUtil.class.getName() + "Business", "BusinessExceptionLog");
    private static final Logger loggerSmppServer = LoggerManager.createLoggerPattern(SmsUtil.class.getName() + "SmppServer", "SmppServerExceptionLog");

    public static String getSmsMessageResponseStatus(int type) {
        String responseStatus = "";
        switch (type) {
            case 0:
                responseStatus = "STATUS_SUCCESS";
                break;
            case 1:
                responseStatus = "STATUS_FAILURE";
                break;
            default:
                responseStatus = "";
        }

        return responseStatus;
    }

    public static String getSmppStatusType(int type) {
        String smppType = "";
        switch (type) {
            case 1:
                smppType = "MT_TEXT";
                break;
            case 2:
                smppType = "MT_BINARY";
                break;
            case 3:
                smppType = "MT_STATUS";
                break;
            default:
                smppType = "MT_STATUS";
        }

        return smppType;
    }

    public static String getSmppStatusMessageState(int state) {
        String messageState = "";
        switch (state) {
            case 0:
                messageState = "STATE_SCHEDULED";
                break;
            case 1:
                messageState = "STATE_ENROUTE";
                break;
            case 2:
                messageState = "STATE_DELIVERED";
                break;
            case 3:
                messageState = "STATE_EXPIRED";
                break;
            case 4:
                messageState = "STATE_DELETED";
                break;
            case 5:
                messageState = "STATE_UNDELIVERABLE";
                break;
            case 6:
                messageState = "STATE_ACCEPTED";
                break;
            case 7:
                messageState = "STATE_UNKNOWN";
                break;
            case 8:
                messageState = "STATE_REJECTED";
                break;
            case 9:
                messageState = "STATE_SKIPPED";
                break;
            default:
                messageState = "STATE_UNKNOWN";
        }

        return messageState;
    }

    public static String getUcpStatusMessageState(String state) {
        String messageState = "";
        if (state.toLowerCase().startsWith("message delivered")) {
            messageState = "STATE_DELIVERED";
        } else if (state.toLowerCase().startsWith("message not delivered")) {
            messageState = "STATE_UNDELIVERABLE";
        } else if (state.toLowerCase().startsWith("message accepted")) {
            messageState = "STATE_ACCEPTED";
        } else if (state.toLowerCase().startsWith("message deleted")) {
            messageState = "STATE_DELETED";
        } else if (state.toLowerCase().startsWith("message enroute")) {
            messageState = "STATE_ENROUTE";
        } else if (state.toLowerCase().startsWith("message expired")) {
            messageState = "STATE_EXPIRED";
        } else if (state.toLowerCase().startsWith("message rejected")) {
            messageState = "STATE_REJECTED";
        } else if (state.toLowerCase().startsWith("message scheduled")) {
            messageState = "STATE_SCHEDULED";
        } else if (state.toLowerCase().startsWith("message skipped")) {
            messageState = "STATE_SKIPPED";
        } else {
            messageState = "STATE_UNKNOWN";
        }

        return messageState;
    }

    public static void logException(Exception exception) {
        logger.error("-----------------------------------------------------------");
        logger.error("ClassName : " + exception.getClass().getName());
        logger.error("Message   : " + exception.getMessage());
        logger.error("Cause     : ", exception.getCause());
        logger.error("-----------------------------------------------------------");
    }

    public static void logException(Exception exception, String id) {
        logger.error("-----------------------------------------------------------");
        logger.error("ClassName : " + exception.getClass().getName());
        logger.error("ClassName : " + id);
        logger.error("Message   : " + exception.getMessage());
        logger.error("Cause     : ", exception.getCause());
        logger.error("-----------------------------------------------------------");
    }

    public static void logExceptionBusiness(Exception exception) {
        loggerBusiness.error("-----------------------------------------------------------");
        loggerBusiness.error("ClassName : " + exception.getClass().getName());
        loggerBusiness.error("Message   : " + exception.getMessage());
        loggerBusiness.error("Cause     : ", exception.getCause());
        loggerBusiness.error("-----------------------------------------------------------");
    }

    public static void logExceptionSmppServer(Exception exception) {
        loggerSmppServer.error("-----------------------------------------------------------");
        loggerSmppServer.error("ClassName : " + exception.getClass().getName());
        loggerSmppServer.error("Message   : " + exception.getMessage());
        loggerSmppServer.error("Cause     : ", exception.getCause());
        loggerSmppServer.error("-----------------------------------------------------------");
    }
}
