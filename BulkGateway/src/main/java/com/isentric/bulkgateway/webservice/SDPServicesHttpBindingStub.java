//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.ser.*;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.encoding.SerializerFactory;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

public class SDPServicesHttpBindingStub extends Stub implements SDPServicesInterface {
    private Vector cachedSerClasses;
    private Vector cachedSerQNames;
    private Vector cachedSerFactories;
    private Vector cachedDeserFactories;
    static OperationDesc[] _operations = new OperationDesc[22];

    private static void _initOperationDesc1() {
        OperationDesc oper = new OperationDesc();
        oper.setName("VsdbAdd");
        ParameterDesc param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_exp_date"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "description"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        oper.setReturnClass(VSDBQueryResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[0] = oper;
        oper = new OperationDesc();
        oper.setName("VsdbRnw");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_exp_date"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        oper.setReturnClass(VSDBQueryResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[1] = oper;
        oper = new OperationDesc();
        oper.setName("VsdbTerm");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        oper.setReturnClass(VSDBQueryResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[2] = oper;
        oper = new OperationDesc();
        oper.setName("VsdbQuery");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        oper.setReturnClass(VSDBQueryResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[3] = oper;
        oper = new OperationDesc();
        oper.setName("SubQueryFile");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SyncResult"));
        oper.setReturnClass(SyncResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[4] = oper;
        oper = new OperationDesc();
        oper.setName("SubQuery");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SubQueryResult"));
        oper.setReturnClass(SubQueryResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[5] = oper;
        oper = new OperationDesc();
        oper.setName("SmsMt");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "charge_party"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "source_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "interactive_session_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "interactive_term_session"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "short_code_suffix_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "short_code_suffix"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "sms_content"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[6] = oper;
        oper = new OperationDesc();
        oper.setName("SmsBulk");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "sms_content"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[7] = oper;
        oper = new OperationDesc();
        oper.setName("MmsMt");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "charge_party"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "source_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "mms_content"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[8] = oper;
        oper = new OperationDesc();
        oper.setName("MmsBulk");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "mms_content"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[9] = oper;
    }

    private static void _initOperationDesc2() {
        OperationDesc oper = new OperationDesc();
        oper.setName("Wappush");
        ParameterDesc param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[10] = oper;
        oper = new OperationDesc();
        oper.setName("WappushBulk");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[11] = oper;
        oper = new OperationDesc();
        oper.setName("SubRmdMt");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "sms_content"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[12] = oper;
        oper = new OperationDesc();
        oper.setName("SubRnwMt");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_rem_txnid"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "sms_content"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[13] = oper;
        oper = new OperationDesc();
        oper.setName("SubRegMt");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[14] = oper;
        oper = new OperationDesc();
        oper.setName("SubSmsMt");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "sms_content"));
        param.setNillable(true);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[15] = oper;
        oper = new OperationDesc();
        oper.setName("SubMmsMt");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "mms_content"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[16] = oper;
        oper = new OperationDesc();
        oper.setName("SubWappush");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[17] = oper;
        oper = new OperationDesc();
        oper.setName("SubTermSvc");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[18] = oper;
        oper = new OperationDesc();
        oper.setName("SubBcSms");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_valid_hour"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_again"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_start_time"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "sms_content"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[19] = oper;
    }

    private static void _initOperationDesc3() {
        OperationDesc oper = new OperationDesc();
        oper.setName("SubBcMms");
        ParameterDesc param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_valid_hour"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_again"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_start_time"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "mms_content"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[20] = oper;
        oper = new OperationDesc();
        oper.setName("SubBcWappush");
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_valid_hour"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_again"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_start_time"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[21] = oper;
    }

    public SDPServicesHttpBindingStub() throws AxisFault {
        this((Service)null);
    }

    public SDPServicesHttpBindingStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public SDPServicesHttpBindingStub(Service service) throws AxisFault {
        this.cachedSerClasses = new Vector();
        this.cachedSerQNames = new Vector();
        this.cachedSerFactories = new Vector();
        this.cachedDeserFactories = new Vector();
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }

        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
        Class beansf = BeanSerializerFactory.class;
        Class beandf = BeanDeserializerFactory.class;
        Class enumsf = EnumSerializerFactory.class;
        Class enumdf = EnumDeserializerFactory.class;
        Class arraysf = ArraySerializerFactory.class;
        Class arraydf = ArrayDeserializerFactory.class;
        Class simplesf = SimpleSerializerFactory.class;
        Class simpledf = SimpleDeserializerFactory.class;
        Class simplelistsf = SimpleListSerializerFactory.class;
        Class simplelistdf = SimpleListDeserializerFactory.class;
        QName qName = new QName("http://xsd.gateway.sdp.digi.com", "AdditionalInfo");
        this.cachedSerQNames.add(qName);
        Class cls = AdditionalInfo.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo");
        this.cachedSerQNames.add(qName);
        cls = AdditionalInfo[].class;
        this.cachedSerClasses.add(cls);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "AdditionalInfo");
        QName qName2 = new QName("http://xsd.gateway.sdp.digi.com", "additional_info");
        this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
        this.cachedDeserFactories.add(new ArrayDeserializerFactory());
        qName = new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent");
        this.cachedSerQNames.add(qName);
        cls = MMSContent[].class;
        this.cachedSerClasses.add(cls);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "MMSContent");
        qName2 = new QName("http://xsd.gateway.sdp.digi.com", "mms_content");
        this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
        this.cachedDeserFactories.add(new ArrayDeserializerFactory());
        qName = new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfQueryResult");
        this.cachedSerQNames.add(qName);
        cls = QueryResult[].class;
        this.cachedSerClasses.add(cls);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "QueryResult");
        qName2 = new QName("http://xsd.gateway.sdp.digi.com", "QueryResult");
        this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
        this.cachedDeserFactories.add(new ArrayDeserializerFactory());
        qName = new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent");
        this.cachedSerQNames.add(qName);
        cls = SMSContent[].class;
        this.cachedSerClasses.add(cls);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "SMSContent");
        qName2 = new QName("http://xsd.gateway.sdp.digi.com", "sms_content");
        this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
        this.cachedDeserFactories.add(new ArrayDeserializerFactory());
        qName = new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType");
        this.cachedSerQNames.add(qName);
        cls = AttachmentType.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "MMSContent");
        this.cachedSerQNames.add(qName);
        cls = MMSContent.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "QueryResult");
        this.cachedSerQNames.add(qName);
        cls = QueryResult.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "SDPResult");
        this.cachedSerQNames.add(qName);
        cls = SDPResult.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "SMSContent");
        this.cachedSerQNames.add(qName);
        cls = SMSContent.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "SubQueryResult");
        this.cachedSerQNames.add(qName);
        cls = SubQueryResult.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "SyncResult");
        this.cachedSerQNames.add(qName);
        cls = SyncResult.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult");
        this.cachedSerQNames.add(qName);
        cls = VSDBQueryResult.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent");
        this.cachedSerQNames.add(qName);
        cls = WAPPushContent.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
    }

    protected Call createCall() throws RemoteException {
        try {
            Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }

            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }

            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }

            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }

            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }

            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }

            Enumeration keys = super.cachedProperties.keys();

            while(keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }

            synchronized(this) {
                if (this.firstCall()) {
                    _call.setEncodingStyle((String)null);

                    for(int i = 0; i < this.cachedSerFactories.size(); ++i) {
                        Class cls = (Class)this.cachedSerClasses.get(i);
                        QName qName = (QName)this.cachedSerQNames.get(i);
                        Object x = this.cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            Class sf = (Class)this.cachedSerFactories.get(i);
                            Class df = (Class)this.cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)this.cachedSerFactories.get(i);
                            DeserializerFactory df = (DeserializerFactory)this.cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }

            return _call;
        } catch (Throwable _t) {
            throw new AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public VSDBQueryResult vsdbAdd(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, Calendar sub_exp_date, String description, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[0]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbAdd"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, keyword, sub_exp_date, description, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (VSDBQueryResult)_resp;
                    } catch (Exception var12) {
                        return (VSDBQueryResult)JavaUtils.convert(_resp, VSDBQueryResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public VSDBQueryResult vsdbRnw(String login_name, String service_id, String cp_id, String destination_mobtel, String sub_id, String keyword, Calendar sub_exp_date, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[1]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbRnw"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, sub_id, keyword, sub_exp_date, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (VSDBQueryResult)_resp;
                    } catch (Exception var12) {
                        return (VSDBQueryResult)JavaUtils.convert(_resp, VSDBQueryResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public VSDBQueryResult vsdbTerm(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[2]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbTerm"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, keyword, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (VSDBQueryResult)_resp;
                    } catch (Exception var10) {
                        return (VSDBQueryResult)JavaUtils.convert(_resp, VSDBQueryResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public VSDBQueryResult vsdbQuery(String login_name, String service_id, String cp_id, String destination_mobtel, String sub_id, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[3]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbQuery"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, sub_id, keyword, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (VSDBQueryResult)_resp;
                    } catch (Exception var11) {
                        return (VSDBQueryResult)JavaUtils.convert(_resp, VSDBQueryResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SyncResult subQueryFile(String login_name, String service_id, String cp_id, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[4]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubQueryFile"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SyncResult)_resp;
                    } catch (Exception var8) {
                        return (SyncResult)JavaUtils.convert(_resp, SyncResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SubQueryResult subQuery(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[5]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubQuery"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, keyword, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SubQueryResult)_resp;
                    } catch (Exception var10) {
                        return (SubQueryResult)JavaUtils.convert(_resp, SubQueryResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult smsMt(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String sender_name, String keyword, String interactive_session_ind, String interactive_term_session, int status, String transaction_id, String ref_id, String short_code_suffix_ind, String short_code_suffix, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[6]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SmsMt"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, price_code, charge_party, source_mobtel, destination_mobtel, sub_id, sender_name, keyword, interactive_session_ind, interactive_term_session, Integer.valueOf(status), transaction_id, ref_id, short_code_suffix_ind, short_code_suffix, Integer.valueOf(notification_ind), response_url, sms_contents, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var25) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult smsBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            System.out.println("smsBulk called");
            Call _call = this.createCall();
            _call.setOperation(_operations[7]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SmsBulk"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, sender_name, ref_id, Integer.valueOf(notification_ind), response_url, sms_contents, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var14) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult mmsMt(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String transaction_id, String ref_id, int notification_ind, String response_url, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[8]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "MmsMt"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, price_code, charge_party, source_mobtel, destination_mobtel, sub_id, sender_name, keyword, Integer.valueOf(status), transaction_id, ref_id, Integer.valueOf(notification_ind), response_url, subject, mms_contents, attachment, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var23) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult mmsBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String subject, String ref_id, int notification_ind, String response_url, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[9]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "MmsBulk"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, sender_name, subject, ref_id, Integer.valueOf(notification_ind), response_url, mms_contents, attachment, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var16) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult wappush(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String keyword, int status, String transaction_id, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[10]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "Wappush"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, price_code, destination_mobtel, sub_id, keyword, Integer.valueOf(status), transaction_id, ref_id, Integer.valueOf(notification_ind), response_url, wappush_content, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var18) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult wappushBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[11]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "WappushBulk"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, ref_id, Integer.valueOf(notification_ind), response_url, wappush_content, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var13) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subRmdMt(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[12]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubRmdMt"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, price_code, destination_mobtel, sub_id, sender_name, keyword, Integer.valueOf(status), ref_id, Integer.valueOf(notification_ind), response_url, sms_contents});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var17) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subRnwMt(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String ref_id, int notification_ind, String response_url, String sub_rem_txnid, SMSContent[] sms_contents) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[13]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubRnwMt"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, price_code, destination_mobtel, sub_id, sender_name, keyword, Integer.valueOf(status), ref_id, Integer.valueOf(notification_ind), response_url, sub_rem_txnid, sms_contents});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var18) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subRegMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String transaction_id, String ref_id, String response_url, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[14]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubRegMt"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, sender_name, keyword, transaction_id, ref_id, response_url, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var14) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subSmsMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[15]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubSmsMt"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, sender_name, keyword, ref_id, Integer.valueOf(notification_ind), response_url, sms_contents, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var15) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subMmsMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String ref_id, int notification_ind, String response_url, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[16]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubMmsMt"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, sender_name, keyword, ref_id, Integer.valueOf(notification_ind), response_url, subject, mms_contents, attachment, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var17) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subWappush(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[17]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubWappush"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, keyword, ref_id, Integer.valueOf(notification_ind), response_url, wappush_content, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var14) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subTermSvc(String login_name, String service_id, String cp_id, String destination_mobtel, String ref_id, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[18]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubTermSvc"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, ref_id, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var10) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subBcSms(String login_name, String service_id, String cp_id, String sender_name, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[19]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubBcSms"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, sender_name, keyword, ref_id, sub_bc_id, Integer.valueOf(sub_bc_valid_hour), sub_bc_again, sub_bc_start_time, sms_contents, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var16) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subBcMms(String login_name, String service_id, String cp_id, String sender_name, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[20]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubBcMms"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, sender_name, keyword, ref_id, sub_bc_id, Integer.valueOf(sub_bc_valid_hour), sub_bc_again, sub_bc_start_time, subject, mms_contents, attachment, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var18) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult subBcWappush(String login_name, String service_id, String cp_id, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[21]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP12_CONSTANTS);
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "SubBcWappush"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, keyword, ref_id, sub_bc_id, Integer.valueOf(sub_bc_valid_hour), sub_bc_again, sub_bc_start_time, wappush_content, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var15) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    static {
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
    }
}
