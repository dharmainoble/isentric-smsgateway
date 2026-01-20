package com.isentric.smsserver.object;

import java.io.Serializable;

public class HttpRequestParamObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String action;
    private String username;
    private String password;
    private String apiKey;
    private String msisdn;
    private String shortcode;
    private String message;
    private String messageId;
    private int dataEncoding;
    private String scheduleDate;
    private boolean requestDlr;
    private String callbackUrl;
    private String clientIp;
    private String requestTime;

    public HttpRequestParamObject() {}

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

    public String getShortcode() { return shortcode; }
    public void setShortcode(String shortcode) { this.shortcode = shortcode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public int getDataEncoding() { return dataEncoding; }
    public void setDataEncoding(int dataEncoding) { this.dataEncoding = dataEncoding; }

    public String getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(String scheduleDate) { this.scheduleDate = scheduleDate; }

    public boolean isRequestDlr() { return requestDlr; }
    public void setRequestDlr(boolean requestDlr) { this.requestDlr = requestDlr; }

    public String getCallbackUrl() { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

    public String getRequestTime() { return requestTime; }
    public void setRequestTime(String requestTime) { this.requestTime = requestTime; }
}
