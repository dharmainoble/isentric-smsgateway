//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecificMessagesObject implements Serializable {
    private static final long serialVersionUID = 1L;
    public String custid;
    public String messages;
    public int status;

    public SpecificMessagesObject() {
    }

    public SpecificMessagesObject(ResultSet rs) throws SQLException {
        this.custid = rs.getString("custid");
        this.messages = rs.getString("messages");
        this.status = rs.getInt("status");
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getCustid() {
        return this.custid;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMessages() {
        return this.messages;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
