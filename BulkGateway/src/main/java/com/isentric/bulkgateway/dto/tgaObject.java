//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

public class tgaObject {
    private String custid;
    private String filter_flag;
    private String server;
    private String dipEngine;

    public String toString() {
        return "tgaObject [custid=" + this.custid + ", filter_flag=" + this.filter_flag + ", server=" + this.server + ", dipEngine=" + this.dipEngine + "]";
    }

    public String getCustid() {
        return this.custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getFilter_flag() {
        return this.filter_flag;
    }

    public void setFilter_flag(String filterFlag) {
        this.filter_flag = filterFlag;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDipEngine() {
        return this.dipEngine;
    }

    public void setDipEngine(String dipEngine) {
        this.dipEngine = dipEngine;
    }
}
