//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.constant;

import java.io.Serializable;

public class SmsConstant implements Serializable {
    private static final long serialVersionUID = -3008843053734182490L;
    public static final String SMSC_SMPP = "smpp";
    public static final int MESSAGE_TYPE_TEXT = 0;
    public static final String TELCO_CELCOM = "celcom";
    public static final String TELCO_DIGI = "digi";
    public static final String TELCO_MAXIS = "maxis";
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILURE = 1;
    private String defaultSmscValue;
    private int defaultMessageTypeValue;

    public int getDefaultMessageTypeValue() {
        this.defaultMessageTypeValue = 0;
        return this.defaultMessageTypeValue;
    }

    public String getDefaultSmscValue() {
        return this.defaultSmscValue;
    }

    public void setDefaultMessageTypeValue(int defaultMessageTypeValue) {
        this.defaultMessageTypeValue = defaultMessageTypeValue;
    }

    public void setDefaultSmscValue(String defaultSmscValue) {
        this.defaultSmscValue = defaultSmscValue;
    }
}
