//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import java.io.Serializable;

public class extMTObject implements Serializable {
    private String billFlag;
    private String shortcode;
    private String custid;
    private String rmsisdn;
    private String smsisdn;
    private String mtid;
    private String mtprice;
    private int productType;
    private String productCode;
    private String keyword;
    private int dataEncoding;
    private String dataStr;
    private String dataUrl;
    private String urlTitle;
    private int dnrep;
    private String groupTag;
    private String ewigFlag;
    private String cFlag;
    private String cpip;

    public String toString() {
        return this.getClass().getName() + " [billFlag=" + this.billFlag + "," + "shortcode=" + this.shortcode + "," + "custid=" + this.custid + "," + "rmsisdn=" + this.rmsisdn + "," + "smsisdn=" + this.smsisdn + "," + "mtid=" + this.mtid + "," + "mtprice=" + this.mtprice + "," + "productType=" + this.productType + "," + "productCode=" + this.productCode + "," + "keyword=" + this.keyword + "," + "dataEncoding=" + this.dataEncoding + "," + "dataStr=" + this.dataStr + "," + "dataUrl=" + this.dataUrl + "," + "urlTitle=" + this.urlTitle + "," + "dnrep=" + this.dnrep + "," + "groupTag=" + this.groupTag + "," + "cFlag=" + this.cFlag + "," + "cpip=" + this.cpip + "," + "ewigFlag=" + this.ewigFlag + "]";
    }

    public String getBillFlag() {
        return this.billFlag;
    }

    public void setBillFlag(String billFlag) {
        this.billFlag = billFlag;
    }

    public String getShortcode() {
        return this.shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getCustId() {
        return this.custid;
    }

    public void setCustId(String custid) {
        this.custid = custid;
    }

    public String getRMsisdn() {
        return this.rmsisdn;
    }

    public void setRMsisdn(String rmsisdn) {
        this.rmsisdn = rmsisdn;
    }

    public String getSMsisdn() {
        return this.smsisdn;
    }

    public void setSMsisdn(String smsisdn) {
        this.smsisdn = smsisdn;
    }

    public String getMtid() {
        return this.mtid;
    }

    public void setMtid(String mtid) {
        this.mtid = mtid;
    }

    public String getMTPrice() {
        return this.mtprice;
    }

    public void setMTPrice(String mtprice) {
        this.mtprice = mtprice;
    }

    public int getProductType() {
        return this.productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getDataEncoding() {
        return this.dataEncoding;
    }

    public void setDataEncoding(int dataEncoding) {
        this.dataEncoding = dataEncoding;
    }

    public String getDataStr() {
        return this.dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public String getDataUrl() {
        return this.dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getURLTitle() {
        return this.urlTitle;
    }

    public void setURLTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    public int getDNRep() {
        return this.dnrep;
    }

    public void setDNRep(int dnrep) {
        this.dnrep = dnrep;
    }

    public String getGroupTag() {
        return this.groupTag;
    }

    public void setGroupTag(String groupTag) {
        this.groupTag = groupTag;
    }

    public String getEwigFlag() {
        return this.ewigFlag;
    }

    public void setEwigFlag(String ewigFlag) {
        this.ewigFlag = ewigFlag;
    }

    public String getCFlag() {
        return this.cFlag;
    }

    public void setCFlag(String flag) {
        this.cFlag = flag;
    }

    public String getCpip() {
        return this.cpip;
    }

    public void setCpip(String cpip) {
        this.cpip = cpip;
    }

    public Object clone() throws CloneNotSupportedException {
        extMTObject cloned = (extMTObject)super.clone();
        return cloned;
    }
}
