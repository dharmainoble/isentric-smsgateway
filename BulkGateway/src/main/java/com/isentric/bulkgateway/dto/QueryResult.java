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
import java.util.Calendar;

public class QueryResult implements Serializable {
    private String mobtel;
    private String sub_id;
    private Calendar sub_reg_date;
    private Calendar sub_exp_date;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(QueryResult.class, true);

    public QueryResult() {
    }

    public QueryResult(String mobtel, String sub_id, Calendar sub_reg_date, Calendar sub_exp_date) {
        this.mobtel = mobtel;
        this.sub_id = sub_id;
        this.sub_reg_date = sub_reg_date;
        this.sub_exp_date = sub_exp_date;
    }

    public String getMobtel() {
        return this.mobtel;
    }

    public void setMobtel(String mobtel) {
        this.mobtel = mobtel;
    }

    public String getSub_id() {
        return this.sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public Calendar getSub_reg_date() {
        return this.sub_reg_date;
    }

    public void setSub_reg_date(Calendar sub_reg_date) {
        this.sub_reg_date = sub_reg_date;
    }

    public Calendar getSub_exp_date() {
        return this.sub_exp_date;
    }

    public void setSub_exp_date(Calendar sub_exp_date) {
        this.sub_exp_date = sub_exp_date;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof QueryResult)) {
            return false;
        } else {
            QueryResult other = (QueryResult)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.mobtel == null && other.getMobtel() == null || this.mobtel != null && this.mobtel.equals(other.getMobtel())) && (this.sub_id == null && other.getSub_id() == null || this.sub_id != null && this.sub_id.equals(other.getSub_id())) && (this.sub_reg_date == null && other.getSub_reg_date() == null || this.sub_reg_date != null && this.sub_reg_date.equals(other.getSub_reg_date())) && (this.sub_exp_date == null && other.getSub_exp_date() == null || this.sub_exp_date != null && this.sub_exp_date.equals(other.getSub_exp_date()));
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
            if (this.getMobtel() != null) {
                _hashCode += this.getMobtel().hashCode();
            }

            if (this.getSub_id() != null) {
                _hashCode += this.getSub_id().hashCode();
            }

            if (this.getSub_reg_date() != null) {
                _hashCode += this.getSub_reg_date().hashCode();
            }

            if (this.getSub_exp_date() != null) {
                _hashCode += this.getSub_exp_date().hashCode();
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
        typeDesc.setXmlType(new QName("http://xsd.gateway.sdp.digi.com", "QueryResult"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("mobtel");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "mobtel"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("sub_id");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("sub_reg_date");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "sub_reg_date"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("sub_exp_date");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "sub_exp_date"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
