//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class OperationError implements Serializable {
    private static final long serialVersionUID = 6817539530986168160L;
    private String code;
    private String message;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
