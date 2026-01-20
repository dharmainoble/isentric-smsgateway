package com.isentric.smsserver.object;

import java.io.Serializable;

public class SMSMessageExtMTObject implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private String date;
    private String price;
    private String datacoding;
    private String cmpContentType;
    private String cFlag;

    public SMSMessageExtMTObject() {}

    public String getShortcode() { return shortcode; }
    public void setShortcode(String shortcode) { this.shortcode = shortcode; }

    public String getCustid() { return custid; }
    public void setCustid(String custid) { this.custid = custid; }

    public String getRmsisdn() { return rmsisdn; }
    public void setRmsisdn(String rmsisdn) { this.rmsisdn = rmsisdn; }

    public String getSmsisdn() { return smsisdn; }
    public void setSmsisdn(String smsisdn) { this.smsisdn = smsisdn; }

    public String getMtid() { return mtid; }
    public void setMtid(String mtid) { this.mtid = mtid; }

    public String getMtprice() { return mtprice; }
    public void setMtprice(String mtprice) { this.mtprice = mtprice; }

    public int getProductType() { return productType; }
    public void setProductType(int productType) { this.productType = productType; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public int getDataEncoding() { return dataEncoding; }
    public void setDataEncoding(int dataEncoding) { this.dataEncoding = dataEncoding; }

    public String getDataStr() { return dataStr; }
    public void setDataStr(String dataStr) { this.dataStr = dataStr; }

    public String getDataUrl() { return dataUrl; }
    public void setDataUrl(String dataUrl) { this.dataUrl = dataUrl; }

    public String getUrlTitle() { return urlTitle; }
    public void setUrlTitle(String urlTitle) { this.urlTitle = urlTitle; }

    public int getDnrep() { return dnrep; }
    public void setDnrep(int dnrep) { this.dnrep = dnrep; }

    public String getGroupTag() { return groupTag; }
    public void setGroupTag(String groupTag) { this.groupTag = groupTag; }

    public String getEwigFlag() { return ewigFlag; }
    public void setEwigFlag(String ewigFlag) { this.ewigFlag = ewigFlag; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getDatacoding() { return datacoding; }
    public void setDatacoding(String datacoding) { this.datacoding = datacoding; }

    public String getCmpContentType() { return cmpContentType; }
    public void setCmpContentType(String cmpContentType) { this.cmpContentType = cmpContentType; }

    public String getCFlag() { return cFlag; }
    public void setCFlag(String cFlag) { this.cFlag = cFlag; }
}
