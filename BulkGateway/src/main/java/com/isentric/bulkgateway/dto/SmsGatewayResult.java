package com.isentric.bulkgateway.dto;

public class SmsGatewayResult {

    private String transactionId;
    private int errorCode;
    private String errorDesc;
    private String errorList;
    private String successList;

    // Getters and Setters

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public String getErrorDesc() { return errorDesc; }
    public void setErrorDesc(String errorDesc) { this.errorDesc = errorDesc; }

    public String getErrorList() { return errorList; }
    public void setErrorList(String errorList) { this.errorList = errorList; }

    public String getSuccessList() { return successList; }
    public void setSuccessList(String successList) { this.successList = successList; }
}
