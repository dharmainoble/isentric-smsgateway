//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.tga.webservice;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

import javax.xml.namespace.QName;
import java.io.Serializable;

public class QSResponse implements Serializable {
    private String msisdn;
    private String regionCode;
    private String respCode;
    private String telco;
    private String txnId;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;
    private static TypeDesc typeDesc = new TypeDesc(QSResponse.class, true);

    public QSResponse() {
    }

    public QSResponse(String msisdn, String regionCode, String respCode, String telco, String txnId) {
        this.msisdn = msisdn;
        this.regionCode = regionCode;
        this.respCode = respCode;
        this.telco = telco;
        this.txnId = txnId;
    }

    public String getMsisdn() {
        return this.msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRegionCode() {
        return this.regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRespCode() {
        return this.respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getTelco() {
        return this.telco;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }

    public String getTxnId() {
        return this.txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof QSResponse)) {
            return false;
        } else {
            QSResponse other = (QSResponse)obj;
            if (obj == null) {
                return false;
            } else if (this == obj) {
                return true;
            } else if (this.__equalsCalc != null) {
                return this.__equalsCalc == obj;
            } else {
                this.__equalsCalc = obj;
                boolean _equals = (this.msisdn == null && other.getMsisdn() == null || this.msisdn != null && this.msisdn.equals(other.getMsisdn())) && (this.regionCode == null && other.getRegionCode() == null || this.regionCode != null && this.regionCode.equals(other.getRegionCode())) && (this.respCode == null && other.getRespCode() == null || this.respCode != null && this.respCode.equals(other.getRespCode())) && (this.telco == null && other.getTelco() == null || this.telco != null && this.telco.equals(other.getTelco())) && (this.txnId == null && other.getTxnId() == null || this.txnId != null && this.txnId.equals(other.getTxnId()));
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
            if (this.getMsisdn() != null) {
                _hashCode += this.getMsisdn().hashCode();
            }

            if (this.getRegionCode() != null) {
                _hashCode += this.getRegionCode().hashCode();
            }

            if (this.getRespCode() != null) {
                _hashCode += this.getRespCode().hashCode();
            }

            if (this.getTelco() != null) {
                _hashCode += this.getTelco().hashCode();
            }

            if (this.getTxnId() != null) {
                _hashCode += this.getTxnId().hashCode();
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
        typeDesc.setXmlType(new QName("http://webservice.qs.mnp.isentric.com", "QSResponse"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("msisdn");
        elemField.setXmlName(new QName("", "msisdn"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("regionCode");
        elemField.setXmlName(new QName("", "regionCode"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("respCode");
        elemField.setXmlName(new QName("", "respCode"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("telco");
        elemField.setXmlName(new QName("", "telco"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new ElementDesc();
        elemField.setFieldName("txnId");
        elemField.setXmlName(new QName("", "txnId"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }
}
