package com.isentric.smsserver.object;

import java.io.Serializable;

public class SMSMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String messageId;
    private String sourceAddress;
    private String destinationAddress;
    private String message;
    private String shortcode;
    private int dataEncoding;
    private String status;
    private String deliveryStatus;
    private String sentDate;
    private String deliveredDate;
    private String errorCode;
    private String errorMessage;
    private int priority;
    private String validityPeriod;
    private boolean requestDeliveryReport;

    public SMSMessage() {}

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getSourceAddress() { return sourceAddress; }
    public void setSourceAddress(String sourceAddress) { this.sourceAddress = sourceAddress; }

    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getShortcode() { return shortcode; }
    public void setShortcode(String shortcode) { this.shortcode = shortcode; }

    public int getDataEncoding() { return dataEncoding; }
    public void setDataEncoding(int dataEncoding) { this.dataEncoding = dataEncoding; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public String getSentDate() { return sentDate; }
    public void setSentDate(String sentDate) { this.sentDate = sentDate; }

    public String getDeliveredDate() { return deliveredDate; }
    public void setDeliveredDate(String deliveredDate) { this.deliveredDate = deliveredDate; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getValidityPeriod() { return validityPeriod; }
    public void setValidityPeriod(String validityPeriod) { this.validityPeriod = validityPeriod; }

    public boolean isRequestDeliveryReport() { return requestDeliveryReport; }
    public void setRequestDeliveryReport(boolean requestDeliveryReport) { this.requestDeliveryReport = requestDeliveryReport; }
}
