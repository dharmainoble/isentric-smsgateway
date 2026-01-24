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

public class SMSContent implements Serializable {
    private String content;
    private String ucp_data_coding_id;
    private String ucp_msg_class;
    private String ucp_msg_type;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(SMSContent.class, true);

    public SMSContent() {
    }

    public SMSContent(String content, String ucp_data_coding_id, String ucp_msg_class, String ucp_msg_type) {
        this.content = content;
        this.ucp_data_coding_id = ucp_data_coding_id;
        this.ucp_msg_class = ucp_msg_class;
        this.ucp_msg_type = ucp_msg_type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUcp_data_coding_id() {
        return this.ucp_data_coding_id;
    }

    public void setUcp_data_coding_id(String ucp_data_coding_id) {
        this.ucp_data_coding_id = ucp_data_coding_id;
    }

    public String getUcp_msg_class() {
        return this.ucp_msg_class;
    }

    public void setUcp_msg_class(String ucp_msg_class) {
        this.ucp_msg_class = ucp_msg_class;
    }

    public String getUcp_msg_type() {
        return this.ucp_msg_type;
    }

    public void setUcp_msg_type(String ucp_msg_type) {
        this.ucp_msg_type = ucp_msg_type;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SMSContent)) {
            return false;
        } else {
            SMSContent other = (SMSContent)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.content == null && other.getContent() == null || this.content != null && this.content.equals(other.getContent())) && (this.ucp_data_coding_id == null && other.getUcp_data_coding_id() == null || this.ucp_data_coding_id != null && this.ucp_data_coding_id.equals(other.getUcp_data_coding_id())) && (this.ucp_msg_class == null && other.getUcp_msg_class() == null || this.ucp_msg_class != null && this.ucp_msg_class.equals(other.getUcp_msg_class())) && (this.ucp_msg_type == null && other.getUcp_msg_type() == null || this.ucp_msg_type != null && this.ucp_msg_type.equals(other.getUcp_msg_type()));
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
            if (this.getContent() != null) {
                _hashCode += this.getContent().hashCode();
            }

            if (this.getUcp_data_coding_id() != null) {
                _hashCode += this.getUcp_data_coding_id().hashCode();
            }

            if (this.getUcp_msg_class() != null) {
                _hashCode += this.getUcp_msg_class().hashCode();
            }

            if (this.getUcp_msg_type() != null) {
                _hashCode += this.getUcp_msg_type().hashCode();
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
        typeDesc.setXmlType(new QName("http://xsd.gateway.sdp.digi.com", "SMSContent"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "content"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("ucp_data_coding_id");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "ucp_data_coding_id"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("ucp_msg_class");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "ucp_msg_class"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("ucp_msg_type");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "ucp_msg_type"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
