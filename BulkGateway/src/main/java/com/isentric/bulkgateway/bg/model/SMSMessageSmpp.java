package com.isentric.bulkgateway.bg.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_smpp_in")
public class SMSMessageSmpp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private int rowId;

    @Column(name = "guid", length = 50, nullable = false)
    private String guid = "";

    @Column(name = "groupId", length = 50, nullable = false)
    private String groupId = "";

    @Column(name = "ip", length = 10, nullable = false)
    private String ip = "";

    @Column(name = "smsc", length = 10, nullable = false)
    private String smsc = "";

    @Column(name = "telco", length = 50, nullable = false)
    private String telco = "";

    @Column(name = "smppName", length = 50, nullable = false)
    private String smppName = "";

    @Column(name = "smppConfig", length = 50, nullable = false)
    private String smppConfig = "";

    @Column(name = "moid", length = 50, nullable = false)
    private String moid = "";

    @Column(name = "sender", length = 15, nullable = false)
    private String sender = "";

    @Column(name = "recipient", length = 15, nullable = false)
    private String recipient = "";

    @Column(name = "senderType", nullable = false)
    private int senderType = 0;

    @Column(name = "keyword", nullable = false)
    private String keyword = "";

    @Column(name = "price", length = 10, nullable = false)
    private String price = "";

    @Column(name = "messageType", nullable = false)
    private int messageType = 0;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status", nullable = false)
    private int status = 0;

    @Column(name = "flag", nullable = false)
    private int flag = 0;

    @Column(name = "error", length = 200, nullable = false)
    private String error = "";

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "message")
    private String message;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "callbackURL")
    private String callbackURL;

    @Column(name = "userGroup", length = 50, nullable = false)
    private String userGroup = "";

    @Column(name = "credit", length = 10)
    private String credit = "0.00";

    @Column(name = "cflag")
    private Integer cFlag = 0;

    @Transient
    private String shortcode;

    @Transient
    public String messageBytes;

    @Transient
    public String property;

    @Transient
    public int type;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSmsc() {
        return smsc;
    }

    public void setSmsc(String smsc) {
        this.smsc = smsc;
    }

    public String getTelco() {
        return telco;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }

    public String getSmppName() {
        return smppName;
    }

    public void setSmppName(String smppName) {
        this.smppName = smppName;
    }

    public String getSmppConfig() {
        return smppConfig;
    }

    public void setSmppConfig(String smppConfig) {
        this.smppConfig = smppConfig;
    }

    public String getMoid() {
        return moid;
    }

    public void setMoid(String moid) {
        this.moid = moid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public Integer getcFlag() {
        return cFlag;
    }

    public void setcFlag(Integer cFlag) {
        this.cFlag = cFlag;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getMessageBytes() {
        return messageBytes;
    }

    public void setMessageBytes(String messageBytes) {
        this.messageBytes = messageBytes;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
