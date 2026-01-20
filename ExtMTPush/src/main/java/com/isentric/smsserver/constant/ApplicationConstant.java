package com.isentric.smsserver.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConstant {

    public static String HOST_IP_LOCAL = "amonisis";
    public static String HOST_IP_AVATAR = "avatar";
    public static String HOST_IP_GENERAL = "general";

    // Content Types
    public static final String CONTENT_TYPE_RINGTONES = "p";
    public static final String CONTENT_TYPE_WALLPAPER = "w";
    public static final String CONTENT_TYPE_GAMES = "j";
    public static final String CONTENT_TYPE_MKARAOKE = "k";
    public static final String CONTENT_TYPE_TRUETONE = "t";
    public static final String CONTENT_TYPE_ANIMATION = "a";
    public static final String CONTENT_TYPE_VIDEO = "v";
    public static final String CONTENT_TYPE_THEME = "h";
    public static final String CONTENT_TYPE_BOOK = "b";
    public static final String CONTENT_TYPE_OTHER = "x";

    // WAP Message Content Types
    public static final String WAP_MESSAGE_CONTENT_TYPE_JAVA_GAMES = "J";
    public static final String WAP_MESSAGE_CONTENT_TYPE_ANIMATION = "A";
    public static final String WAP_MESSAGE_CONTENT_TYPE_POLY = "P";
    public static final String WAP_MESSAGE_CONTENT_TYPE_WALLPAPER = "W";
    public static final String WAP_MESSAGE_CONTENT_TYPE_TRUETONE = "O";
    public static final String WAP_MESSAGE_CONTENT_TYPE_MKARAOKE = "K";
    public static final String WAP_MESSAGE_CONTENT_TYPE_VIDEO = "V";
    public static final String WAP_MESSAGE_CONTENT_TYPE_LOGOS = "L";
    public static final String WAP_MESSAGE_CONTENT_TYPE_GRAPHICS = "G";
    public static final String WAP_MESSAGE_CONTENT_TYPE_SCREEN = "S";
    public static final String WAP_MESSAGE_CONTENT_TYPE_MONO = "M";
    public static final String WAP_MESSAGE_CONTENT_TYPE_THEME = "T";
    public static final String WAP_MESSAGE_CONTENT_TYPE_BOOK = "B";

    // Message Types
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_PDU = 1;
    public static final int TYPE_OTA = 2;
    public static final int TYPE_RINGTONE = 3;
    public static final int TYPE_OPERATORLOGO = 4;
    public static final int TYPE_PICTURE = 5;
    public static final int TYPE_CHINESE = 6;
    public static final int TYPE_EWIG = 7;
    public static final int TYPE_TEXTCARD = 8;

    // WSDL Endpoints
    public static String GENERICCREDIT_WSDL = "http://localhost:8080/services/GenericCredit";
    public static String EXTMTPUSH_WSDL = "http://localhost:8080/services/ExternalMTPushInterface";
    public static String BULKGATEWAY_WSDL = "http://192.168.26.163:8180/BGAPI/BulkGatewayServiceAPI";
    public static String CHECKSMSUSER_WSDL = "http://localhost:8080/services/CheckSMSUser";

    public static final String CONNECTION_FACTORY_CONTEXT = "ConnectionFactory";
    public static final String BULK_ENCODE_QUERY = "select key_1 from key_db.key_table where row_id =1";

    @Value("${app.host.ip.local:amonisis}")
    public void setHostIpLocal(String hostIpLocal) {
        HOST_IP_LOCAL = hostIpLocal;
    }

    @Value("${app.host.ip.avatar:avatar}")
    public void setHostIpAvatar(String hostIpAvatar) {
        HOST_IP_AVATAR = hostIpAvatar;
    }

    @Value("${app.host.ip.general:general}")
    public void setHostIpGeneral(String hostIpGeneral) {
        HOST_IP_GENERAL = hostIpGeneral;
    }
}

