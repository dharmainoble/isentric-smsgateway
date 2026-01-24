//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import java.io.Serializable;

public class ChargeObj implements Serializable {
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
    private String loginName;
    private String password;
    private String cpId;
    private String serviceId;
    private String responseURL;
    private int notificationId;
    private String chargeParty;
    private String keyword;
    private String deliveryChannel;

    public ChargeObj(String httpName, String configFile, String URL, String chText, String enText, String binary, String msgHeader, String sessionID, String DNRefID, String setProperty, String dynamicTxID, String loginName, String password, String cpId, String serviceId, String responseURL, int notificationId, String chargeParty, String keyword, String deliveryChannel) {
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
        this.loginName = loginName;
        this.password = password;
        this.cpId = cpId;
        this.serviceId = serviceId;
        this.responseURL = responseURL;
        this.notificationId = notificationId;
        this.chargeParty = chargeParty;
        this.keyword = keyword;
        this.deliveryChannel = deliveryChannel;
    }

    public String getHttpName() {
        return this.httpName;
    }

    public void setHttpName(String httpName) {
        this.httpName = httpName;
    }

    public String getConfigFile() {
        return this.configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public String getChText() {
        return this.chText;
    }

    public void setChText(String chText) {
        this.chText = chText;
    }

    public String getEnText() {
        return this.enText;
    }

    public void setEnText(String enText) {
        this.enText = enText;
    }

    public String getBinary() {
        return this.binary;
    }

    public void setBinary(String binary) {
        this.binary = binary;
    }

    public String getMsgHeader() {
        return this.msgHeader;
    }

    public void setMsgHeader(String msgHeader) {
        this.msgHeader = msgHeader;
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

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpId() {
        return this.cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getResponseURL() {
        return this.responseURL;
    }

    public void setResponseURL(String responseURL) {
        this.responseURL = responseURL;
    }

    public int getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getChargeParty() {
        return this.chargeParty;
    }

    public void setChargeParty(String chargeParty) {
        this.chargeParty = chargeParty;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDeliveryChannel() {
        return this.deliveryChannel;
    }

    public void setDeliveryChannel(String deliveryChannel) {
        this.deliveryChannel = deliveryChannel;
    }
}
