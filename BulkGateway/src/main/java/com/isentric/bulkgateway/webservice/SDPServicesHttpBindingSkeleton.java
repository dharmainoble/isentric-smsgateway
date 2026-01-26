//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.wsdl.Skeleton;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.util.*;

public class SDPServicesHttpBindingSkeleton implements SDPServicesInterface, Skeleton {
    private SDPServicesInterface impl;
    private static Map _myOperations = new Hashtable();
    private static Collection _myOperationsList = new ArrayList();

    public static List getOperationDescByName(String methodName) {
        return (List)_myOperations.get(methodName);
    }

    public static Collection getOperationDescs() {
        return _myOperationsList;
    }

    public SDPServicesHttpBindingSkeleton() {
        this.impl = new SDPServicesHttpBindingImpl();
    }

    public SDPServicesHttpBindingSkeleton(SDPServicesInterface impl) {
        this.impl = impl;
    }

    public VSDBQueryResult vsdbAdd(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, Calendar sub_exp_date, String description, AdditionalInfo[] array_of_info) throws RemoteException {
        VSDBQueryResult ret = this.impl.vsdbAdd(login_name, service_id, cp_id, destination_mobtel, keyword, sub_exp_date, description, array_of_info);
        return ret;
    }

    public VSDBQueryResult vsdbRnw(String login_name, String service_id, String cp_id, String destination_mobtel, String sub_id, String keyword, Calendar sub_exp_date, AdditionalInfo[] array_of_info) throws RemoteException {
        VSDBQueryResult ret = this.impl.vsdbRnw(login_name, service_id, cp_id, destination_mobtel, sub_id, keyword, sub_exp_date, array_of_info);
        return ret;
    }

    public VSDBQueryResult vsdbTerm(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        VSDBQueryResult ret = this.impl.vsdbTerm(login_name, service_id, cp_id, destination_mobtel, keyword, array_of_info);
        return ret;
    }

    public VSDBQueryResult vsdbQuery(String login_name, String service_id, String cp_id, String destination_mobtel, String sub_id, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        VSDBQueryResult ret = this.impl.vsdbQuery(login_name, service_id, cp_id, destination_mobtel, sub_id, keyword, array_of_info);
        return ret;
    }

    public SyncResult subQueryFile(String login_name, String service_id, String cp_id, AdditionalInfo[] array_of_info) throws RemoteException {
        SyncResult ret = this.impl.subQueryFile(login_name, service_id, cp_id, array_of_info);
        return ret;
    }

    public SubQueryResult subQuery(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, AdditionalInfo[] array_of_info) throws RemoteException {
        SubQueryResult ret = this.impl.subQuery(login_name, service_id, cp_id, destination_mobtel, keyword, array_of_info);
        return ret;
    }

    public SDPResult smsMt(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String sender_name, String keyword, String interactive_session_ind, String interactive_term_session, int status, String transaction_id, String ref_id, String short_code_suffix_ind, String short_code_suffix, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.smsMt(login_name, service_id, cp_id, price_code, charge_party, source_mobtel, destination_mobtel, sub_id, sender_name, keyword, interactive_session_ind, interactive_term_session, status, transaction_id, ref_id, short_code_suffix_ind, short_code_suffix, notification_ind, response_url, sms_contents, array_of_info);
        return ret;
    }

    public SDPResult smsBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.smsBulk(login_name, service_id, cp_id, destination_mobtel, sender_name, ref_id, notification_ind, response_url, sms_contents, array_of_info);
        return ret;
    }

    public SDPResult mmsMt(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String transaction_id, String ref_id, int notification_ind, String response_url, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.mmsMt(login_name, service_id, cp_id, price_code, charge_party, source_mobtel, destination_mobtel, sub_id, sender_name, keyword, status, transaction_id, ref_id, notification_ind, response_url, subject, mms_contents, attachment, array_of_info);
        return ret;
    }

    public SDPResult mmsBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String subject, String ref_id, int notification_ind, String response_url, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.mmsBulk(login_name, service_id, cp_id, destination_mobtel, sender_name, subject, ref_id, notification_ind, response_url, mms_contents, attachment, array_of_info);
        return ret;
    }

    public SDPResult wappush(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String keyword, int status, String transaction_id, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.wappush(login_name, service_id, cp_id, price_code, destination_mobtel, sub_id, keyword, status, transaction_id, ref_id, notification_ind, response_url, wappush_content, array_of_info);
        return ret;
    }

    public SDPResult wappushBulk(String login_name, String service_id, String cp_id, String destination_mobtel, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.wappushBulk(login_name, service_id, cp_id, destination_mobtel, ref_id, notification_ind, response_url, wappush_content, array_of_info);
        return ret;
    }

    public SDPResult subRmdMt(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents) throws RemoteException {
        SDPResult ret = this.impl.subRmdMt(login_name, service_id, cp_id, price_code, destination_mobtel, sub_id, sender_name, keyword, status, ref_id, notification_ind, response_url, sms_contents);
        return ret;
    }

    public SDPResult subRnwMt(String login_name, String service_id, String cp_id, String price_code, String destination_mobtel, String sub_id, String sender_name, String keyword, int status, String ref_id, int notification_ind, String response_url, String sub_rem_txnid, SMSContent[] sms_contents) throws RemoteException {
        SDPResult ret = this.impl.subRnwMt(login_name, service_id, cp_id, price_code, destination_mobtel, sub_id, sender_name, keyword, status, ref_id, notification_ind, response_url, sub_rem_txnid, sms_contents);
        return ret;
    }

    public SDPResult subRegMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String transaction_id, String ref_id, String response_url, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subRegMt(login_name, service_id, cp_id, destination_mobtel, sender_name, keyword, transaction_id, ref_id, response_url, array_of_info);
        return ret;
    }

    public SDPResult subSmsMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String ref_id, int notification_ind, String response_url, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subSmsMt(login_name, service_id, cp_id, destination_mobtel, sender_name, keyword, ref_id, notification_ind, response_url, sms_contents, array_of_info);
        return ret;
    }

    public SDPResult subMmsMt(String login_name, String service_id, String cp_id, String destination_mobtel, String sender_name, String keyword, String ref_id, int notification_ind, String response_url, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subMmsMt(login_name, service_id, cp_id, destination_mobtel, sender_name, keyword, ref_id, notification_ind, response_url, subject, mms_contents, attachment, array_of_info);
        return ret;
    }

    public SDPResult subWappush(String login_name, String service_id, String cp_id, String destination_mobtel, String keyword, String ref_id, int notification_ind, String response_url, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subWappush(login_name, service_id, cp_id, destination_mobtel, keyword, ref_id, notification_ind, response_url, wappush_content, array_of_info);
        return ret;
    }

    public SDPResult subTermSvc(String login_name, String service_id, String cp_id, String destination_mobtel, String ref_id, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subTermSvc(login_name, service_id, cp_id, destination_mobtel, ref_id, array_of_info);
        return ret;
    }

    public SDPResult subBcSms(String login_name, String service_id, String cp_id, String sender_name, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, SMSContent[] sms_contents, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subBcSms(login_name, service_id, cp_id, sender_name, keyword, ref_id, sub_bc_id, sub_bc_valid_hour, sub_bc_again, sub_bc_start_time, sms_contents, array_of_info);
        return ret;
    }

    public SDPResult subBcMms(String login_name, String service_id, String cp_id, String sender_name, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, String subject, MMSContent[] mms_contents, AttachmentType attachment, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subBcMms(login_name, service_id, cp_id, sender_name, keyword, ref_id, sub_bc_id, sub_bc_valid_hour, sub_bc_again, sub_bc_start_time, subject, mms_contents, attachment, array_of_info);
        return ret;
    }

    public SDPResult subBcWappush(String login_name, String service_id, String cp_id, String keyword, String ref_id, String sub_bc_id, int sub_bc_valid_hour, String sub_bc_again, Calendar sub_bc_start_time, WAPPushContent wappush_content, AdditionalInfo[] array_of_info) throws RemoteException {
        SDPResult ret = this.impl.subBcWappush(login_name, service_id, cp_id, keyword, ref_id, sub_bc_id, sub_bc_valid_hour, sub_bc_again, sub_bc_start_time, wappush_content, array_of_info);
        return ret;
    }

    static {
        ParameterDesc[] _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_exp_date"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "description"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        OperationDesc _oper = new OperationDesc("vsdbAdd", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbAdd"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("vsdbAdd") == null) {
            _myOperations.put("vsdbAdd", new ArrayList());
        }

        ((List)_myOperations.get("vsdbAdd")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_exp_date"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("vsdbRnw", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbRnw"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("vsdbRnw") == null) {
            _myOperations.put("vsdbRnw", new ArrayList());
        }

        ((List)_myOperations.get("vsdbRnw")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("vsdbTerm", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbTerm"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("vsdbTerm") == null) {
            _myOperations.put("vsdbTerm", new ArrayList());
        }

        ((List)_myOperations.get("vsdbTerm")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("vsdbQuery", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "VSDBQueryResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "VsdbQuery"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("vsdbQuery") == null) {
            _myOperations.put("vsdbQuery", new ArrayList());
        }

        ((List)_myOperations.get("vsdbQuery")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subQueryFile", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SyncResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubQueryFile"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subQueryFile") == null) {
            _myOperations.put("subQueryFile", new ArrayList());
        }

        ((List)_myOperations.get("subQueryFile")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subQuery", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SubQueryResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubQuery"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subQuery") == null) {
            _myOperations.put("subQuery", new ArrayList());
        }

        ((List)_myOperations.get("subQuery")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "charge_party"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "source_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "interactive_session_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "interactive_term_session"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "short_code_suffix_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "short_code_suffix"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("smsMt", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SmsMt"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("smsMt") == null) {
            _myOperations.put("smsMt", new ArrayList());
        }

        ((List)_myOperations.get("smsMt")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("smsBulk", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SmsBulk"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("smsBulk") == null) {
            _myOperations.put("smsBulk", new ArrayList());
        }

        ((List)_myOperations.get("smsBulk")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "charge_party"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "source_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("mmsMt", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "MmsMt"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("mmsMt") == null) {
            _myOperations.put("mmsMt", new ArrayList());
        }

        ((List)_myOperations.get("mmsMt")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("mmsBulk", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "MmsBulk"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("mmsBulk") == null) {
            _myOperations.put("mmsBulk", new ArrayList());
        }

        ((List)_myOperations.get("mmsBulk")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("wappush", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "Wappush"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("wappush") == null) {
            _myOperations.put("wappush", new ArrayList());
        }

        ((List)_myOperations.get("wappush")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("wappushBulk", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "WappushBulk"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("wappushBulk") == null) {
            _myOperations.put("wappushBulk", new ArrayList());
        }

        ((List)_myOperations.get("wappushBulk")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false)};
        _oper = new OperationDesc("subRmdMt", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubRmdMt"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subRmdMt") == null) {
            _myOperations.put("subRmdMt", new ArrayList());
        }

        ((List)_myOperations.get("subRmdMt")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "price_code"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_rem_txnid"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false)};
        _oper = new OperationDesc("subRnwMt", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubRnwMt"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subRnwMt") == null) {
            _myOperations.put("subRnwMt", new ArrayList());
        }

        ((List)_myOperations.get("subRnwMt")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subRegMt", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubRegMt"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subRegMt") == null) {
            _myOperations.put("subRegMt", new ArrayList());
        }

        ((List)_myOperations.get("subRegMt")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subSmsMt", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubSmsMt"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subSmsMt") == null) {
            _myOperations.put("subSmsMt", new ArrayList());
        }

        ((List)_myOperations.get("subSmsMt")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subMmsMt", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubMmsMt"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subMmsMt") == null) {
            _myOperations.put("subMmsMt", new ArrayList());
        }

        ((List)_myOperations.get("subMmsMt")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "notification_ind"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "response_url"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subWappush", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubWappush"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subWappush") == null) {
            _myOperations.put("subWappush", new ArrayList());
        }

        ((List)_myOperations.get("subWappush")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subTermSvc", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubTermSvc"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subTermSvc") == null) {
            _myOperations.put("subTermSvc", new ArrayList());
        }

        ((List)_myOperations.get("subTermSvc")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_valid_hour"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_again"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_start_time"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfSMSContent"), SMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subBcSms", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubBcSms"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subBcSms") == null) {
            _myOperations.put("subBcSms", new ArrayList());
        }

        ((List)_myOperations.get("subBcSms")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sender_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_valid_hour"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_again"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_start_time"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "subject"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "mms_contents"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfMMSContent"), MMSContent[].class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "attachment"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "AttachmentType"), AttachmentType.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subBcMms", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubBcMms"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subBcMms") == null) {
            _myOperations.put("subBcMms", new ArrayList());
        }

        ((List)_myOperations.get("subBcMms")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_valid_hour"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_again"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "sub_bc_start_time"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "dateTime"), Calendar.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "wappush_content"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "WAPPushContent"), WAPPushContent.class, false, false), new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false)};
        _oper = new OperationDesc("subBcWappush", _params, new QName("http://xsd.gateway.sdp.digi.com", "return"));
        _oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        _oper.setElementQName(new QName("http://xsd.gateway.sdp.digi.com", "SubBcWappush"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("subBcWappush") == null) {
            _myOperations.put("subBcWappush", new ArrayList());
        }

        ((List)_myOperations.get("subBcWappush")).add(_oper);
    }
}
