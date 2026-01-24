package com.isentric.bulkgateway.dto;

import java.io.Serializable;

public class HttpObj implements Serializable {
    private static final long serialVersionUID = 1L;
    private String httpName;
    private String configFile;
    private String URL;
    private String chText;
    private String enText;
    private String binary;
    private String msgHeader;
    private String sessionID;
    private String DNRefID;
    private String setProperty;
    private String dynamicTxID;

    public HttpObj(String httpName, String configFile, String URL, String chText, String enText, String binary, String msgHeader, String sessionID, String DNRefID, String setProperty, String dynamicTxID) {
        this.httpName = httpName;
        this.configFile = configFile;
        this.URL = URL;
        this.chText = chText;
        this.enText = enText;
        this.binary = binary;
        this.msgHeader = msgHeader;
        this.sessionID = sessionID;
        this.DNRefID = DNRefID;
        this.setProperty = setProperty;
        this.dynamicTxID = dynamicTxID;
    }

    public void setHttpName(String httpName) {
        this.httpName = httpName;
    }

    public String getHttpName() {
        return this.httpName;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getConfigFile() {
        return this.configFile;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return this.URL;
    }

    public void setChText(String chText) {
        this.chText = chText;
    }

    public String getChText() {
        return this.chText;
    }

    public void setEnText(String enText) {
        this.enText = enText;
    }

    public String getEnText() {
        return this.enText;
    }

    public String getMsgHeader() {
        return this.msgHeader;
    }

    public void setMsgHeader(String msgHeader) {
        this.msgHeader = msgHeader;
    }

    public String getBinary() {
        return this.binary;
    }

    public void setBinary(String binary) {
        this.binary = binary;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getDNRefID() {
        return this.DNRefID;
    }

    public void setDNRefID(String refID) {
        this.DNRefID = refID;
    }

    public String getSetProperty() {
        return this.setProperty;
    }

    public void setSetProperty(String setProperty) {
        this.setProperty = setProperty;
    }

    public String getDynamicTxID() {
        return this.dynamicTxID;
    }

    public void setDynamicTxID(String dynamicTxID) {
        this.dynamicTxID = dynamicTxID;
    }
}
