package com.isentric.bulkgateway.dto;

public class VoidMessagesPrefix {
    private String custId;
    private String enable_flag;
    private String is_specific_prefix;
    private String prefix;

    public VoidMessagesPrefix() {
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getEnable_flag() {
        return enable_flag;
    }

    public void setEnable_flag(String enable_flag) {
        this.enable_flag = enable_flag;
    }

    public String getIs_specific_prefix() {
        return is_specific_prefix;
    }

    public void setIs_specific_prefix(String is_specific_prefix) {
        this.is_specific_prefix = is_specific_prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

