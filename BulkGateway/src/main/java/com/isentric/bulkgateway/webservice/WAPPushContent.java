//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

import javax.xml.namespace.QName;
import java.io.Serializable;

public class WAPPushContent implements Serializable {
    private String charset;
    private String content;
    private String url;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(WAPPushContent.class, true);

    public WAPPushContent() {
    }

    public WAPPushContent(String charset, String content, String url) {
        this.charset = charset;
        this.content = content;
        this.url = url;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof WAPPushContent)) {
            return false;
        } else {
            WAPPushContent other = (WAPPushContent)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.charset == null && other.getCharset() == null || this.charset != null && this.charset.equals(other.getCharset())) && (this.content == null && other.getContent() == null || this.content != null && this.content.equals(other.getContent())) && (this.url == null && other.getUrl() == null || this.url != null && this.url.equals(other.getUrl()));
                this.__equalsCalc = null;
                return _equals;
            }
        }
    }

    public synchronized int hashCode() {
        if (this.__hashCodeCalc) {
            return 0;
        } else {
            this.__hashCodeCalc = true;
            int _hashCode = 1;
            if (this.getCharset() != null) {
                _hashCode += this.getCharset().hashCode();
            }

            if (this.getContent() != null) {
                _hashCode += this.getContent().hashCode();
            }

            if (this.getUrl() != null) {
                _hashCode += this.getUrl().hashCode();
            }

            this.__hashCodeCalc = false;
            return _hashCode;
        }
    }

    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }

    public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType) {
        return new BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType) {
        return new BeanDeserializer(_javaType, _xmlType, typeDesc);
    }

    static {
        typeDesc.setXmlType(new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("charset");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "charset"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "content"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "url"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
