//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Prefix implements Serializable {
    private static final long serialVersionUID = 1L;
    public String route;
    public String country;
    public String credit;
    public int tgaToggle;

    public Prefix() {
    }

    public Prefix(ResultSet rs) throws SQLException {
        this.country = rs.getString("country");
        this.route = rs.getString("route");
        this.credit = rs.getString("credit");
        this.tgaToggle = rs.getInt("tga_toggle");
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoute() {
        return this.route;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCredit() {
        return this.credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public int getTgaToggle() {
        return this.tgaToggle;
    }

    public void setTgaToggle(int tgaToggle) {
        this.tgaToggle = tgaToggle;
    }
}
