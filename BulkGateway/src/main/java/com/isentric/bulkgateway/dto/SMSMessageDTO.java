package com.isentric.bulkgateway.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SMSMessageDTO {

    private static final long serialVersionUID = 4162319008043567220L;
    private int rowId;
    private String guid;
    private String groupId;
    private String ip;
    private String smsc;
    private String telco;
    private String smppName;
    private String smppConfig;
    private String moid;
    private String sender;
    private String recipient;
    private int senderType;
    private String keyword;
    private String message;
    private byte[] messageBytes;
    private int messageType;
    private Date date;
    private String price;
    private String callbackURL;
    private String shortcode;
    private String userGroup;
    private int queueSequence = 0;
    private int cFlag = 0;
}
