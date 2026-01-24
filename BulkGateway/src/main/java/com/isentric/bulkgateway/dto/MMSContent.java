//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

import javax.xml.namespace.QName;
import java.io.Serializable;

public class MMSContent implements Serializable {
    private String charset;
    private String content_type;
    private String filename;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(MMSContent.class, true);

    public MMSContent() {
    }

    public MMSContent(String charset, String content_type, String filename) {
        this.charset = charset;
        this.content_type = content_type;
        this.filename = filename;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent_type() {
        return this.content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof MMSContent)) {
            return false;
        } else {
            MMSContent other = (MMSContent)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.charset == null && other.getCharset() == null || this.charset != null && this.charset.equals(other.getCharset())) && (this.content_type == null && other.getContent_type() == null || this.content_type != null && this.content_type.equals(other.getContent_type())) && (this.filename == null && other.getFilename() == null || this.filename != null && this.filename.equals(other.getFilename()));
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

            if (this.getContent_type() != null) {
                _hashCode += this.getContent_type().hashCode();
            }

            if (this.getFilename() != null) {
                _hashCode += this.getFilename().hashCode();
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
        typeDesc.setXmlType(new QName("http://xsd.gateway.sdp.digi.com", "MMSContent"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("charset");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "charset"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("content_type");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "content_type"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("filename");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "filename"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
