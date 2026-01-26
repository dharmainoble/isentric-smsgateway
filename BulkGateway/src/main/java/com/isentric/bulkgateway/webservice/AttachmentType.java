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
import java.lang.reflect.Array;
import java.util.Arrays;

public class AttachmentType implements Serializable {
    private String fileName;
    private byte[] binaryData;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(AttachmentType.class, true);

    public AttachmentType() {
    }

    public AttachmentType(String fileName, byte[] binaryData) {
        this.fileName = fileName;
        this.binaryData = binaryData;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBinaryData() {
        return this.binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof AttachmentType)) {
            return false;
        } else {
            AttachmentType other = (AttachmentType)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.fileName == null && other.getFileName() == null || this.fileName != null && this.fileName.equals(other.getFileName())) && (this.binaryData == null && other.getBinaryData() == null || this.binaryData != null && Arrays.equals(this.binaryData, other.getBinaryData()));
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
            if (this.getFileName() != null) {
                _hashCode += this.getFileName().hashCode();
            }

            if (this.getBinaryData() != null) {
                for(int i = 0; i < Array.getLength(this.getBinaryData()); ++i) {
                    Object obj = Array.get(this.getBinaryData(), i);
                    if (obj != null && !obj.getClass().isArray()) {
                        _hashCode += obj.hashCode();
                    }
                }
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
        typeDesc.setXmlType(new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("fileName");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "fileName"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("binaryData");
        elemField.setXmlName(new QName("http://xsd.gateway.sdp.digi.com", "binaryData"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
