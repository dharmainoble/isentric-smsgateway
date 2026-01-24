//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecificRouteObject implements Serializable {
    private static final long serialVersionUID = 1L;
    public String route;
    public String telco;
    public String custid;
    public int tgaToggle;

    public SpecificRouteObject() {
    }

    public SpecificRouteObject(ResultSet rs) throws SQLException {
        this.custid = rs.getString("custid");
        this.telco = rs.getString("telco");
        this.route = rs.getString("route");
        this.tgaToggle = rs.getInt("tga_toggle");
    }

    public String getClient() {
        return this.custid;
    }

    public void setClient(String custid) {
        this.custid = custid;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoute() {
        return this.route;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }

    public String getTelco() {
        return this.telco;
    }

    public int getTgaToggle() {
        return this.tgaToggle;
    }

    public void setTgaToggle(int tgaToggle) {
        this.tgaToggle = tgaToggle;
    }
}
