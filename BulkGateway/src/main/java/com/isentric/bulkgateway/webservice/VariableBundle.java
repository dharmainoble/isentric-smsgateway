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

public class VariableBundle implements Serializable {
    private String cp_id;
    private String service_id;
    private int price_split;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(VariableBundle.class, true);

    public VariableBundle() {
    }

    public VariableBundle(String cp_id, String service_id, int price_split) {
        this.cp_id = cp_id;
        this.service_id = service_id;
        this.price_split = price_split;
    }

    public String getCp_id() {
        return this.cp_id;
    }

    public void setCp_id(String cp_id) {
        this.cp_id = cp_id;
    }

    public String getService_id() {
        return this.service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public int getPrice_split() {
        return this.price_split;
    }

    public void setPrice_split(int price_split) {
        this.price_split = price_split;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof VariableBundle)) {
            return false;
        } else {
            VariableBundle other = (VariableBundle)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.cp_id == null && other.getCp_id() == null || this.cp_id != null && this.cp_id.equals(other.getCp_id())) && (this.service_id == null && other.getService_id() == null || this.service_id != null && this.service_id.equals(other.getService_id())) && this.price_split == other.getPrice_split();
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
            if (this.getCp_id() != null) {
                _hashCode += this.getCp_id().hashCode();
            }

            if (this.getService_id() != null) {
                _hashCode += this.getService_id().hashCode();
            }

            _hashCode += this.getPrice_split();
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
        typeDesc.setXmlType(new QName("http://xsd.gateway.sdp.digi.com", "VariableBundle"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("cp_id");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("service_id");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "service_id"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("price_split");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "price_split"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
