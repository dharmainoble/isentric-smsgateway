//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaxisDNStatusObject implements Serializable {
    private static final long serialVersionUID = 1L;
    public String cpidentity;
    public String maxisdn_flag;

    public MaxisDNStatusObject() {
    }

    public MaxisDNStatusObject(ResultSet rs) throws SQLException {
        this.cpidentity = rs.getString("cpidentity");
        this.maxisdn_flag = rs.getString("maxisdn_flag");
    }

    public String getCpidentity() {
        return this.cpidentity;
    }

    public void setCpidentity(String cpidentity) {
        this.cpidentity = cpidentity;
    }

    public String getMaxisdn_flag() {
        return this.maxisdn_flag;
    }

    public void setMaxisdn_flag(String maxisdn_flag) {
        this.maxisdn_flag = maxisdn_flag;
    }
}
