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

public class SubQueryResult implements Serializable {
    private String transaction_id;
    private int error_code;
    private String error_desc;
    private String members_list;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(SubQueryResult.class, true);

    public SubQueryResult() {
    }

    public SubQueryResult(String transaction_id, int error_code, String error_desc, String members_list) {
        this.transaction_id = transaction_id;
        this.error_code = error_code;
        this.error_desc = error_desc;
        this.members_list = members_list;
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getError_code() {
        return this.error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return this.error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public String getMembers_list() {
        return this.members_list;
    }

    public void setMembers_list(String members_list) {
        this.members_list = members_list;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SubQueryResult)) {
            return false;
        } else {
            SubQueryResult other = (SubQueryResult)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.transaction_id == null && other.getTransaction_id() == null || this.transaction_id != null && this.transaction_id.equals(other.getTransaction_id())) && this.error_code == other.getError_code() && (this.error_desc == null && other.getError_desc() == null || this.error_desc != null && this.error_desc.equals(other.getError_desc())) && (this.members_list == null && other.getMembers_list() == null || this.members_list != null && this.members_list.equals(other.getMembers_list()));
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
            if (this.getTransaction_id() != null) {
                _hashCode += this.getTransaction_id().hashCode();
            }

            _hashCode += this.getError_code();
            if (this.getError_desc() != null) {
                _hashCode += this.getError_desc().hashCode();
            }

            if (this.getMembers_list() != null) {
                _hashCode += this.getMembers_list().hashCode();
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
        typeDesc.setXmlType(new QName("http://xsd.gateway.sdp.digi.com", "SubQueryResult"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("transaction_id");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("error_code");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "error_code"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("error_desc");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "error_desc"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("members_list");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "members_list"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
