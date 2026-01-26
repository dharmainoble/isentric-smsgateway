//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.extmt.webservice;

import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;

public class ExternalMTPushInterfaceSoapBindingStub extends Stub implements ExternalMTPushInterface {
    private Vector cachedSerClasses;
    private Vector cachedSerQNames;
    private Vector cachedSerFactories;
    private Vector cachedDeserFactories;
    static OperationDesc[] _operations = new OperationDesc[5];

    static {
        OperationDesc oper = new OperationDesc();
        oper.setName("HLRLookup");
        oper.addParameter(new QName("", "shortcode"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "custid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "rmsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "remoteAdd"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "outputType"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "invokeLive"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("", "HLRLookupReturn"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[0] = oper;
        oper = new OperationDesc();
        oper.setName("receiveExtMTPush");
        oper.addParameter(new QName("", "shortcode"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "custid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "rmsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "smsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageprice"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "product_type"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "product_code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "keyword"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_encoding"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "data_str"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_url"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "dnrep"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "group_tag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "remoteAdd"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "urltitle"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "ewigFlag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(Integer.TYPE);
        oper.setReturnQName(new QName("", "ReceiveExtMTPushReturn"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[1] = oper;
        oper = new OperationDesc();
        oper.setName("receiveExtMTPush");
        oper.addParameter(new QName("", "shortcode"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "custid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "rmsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "smsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageprice"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "product_type"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "product_code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "keyword"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_encoding"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "data_str"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_url"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "dnrep"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "group_tag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "remoteAdd"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(Integer.TYPE);
        oper.setReturnQName(new QName("", "ReceiveExtMTPushReturn"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[2] = oper;
        oper = new OperationDesc();
        oper.setName("receiveExtMTPush");
        oper.addParameter(new QName("", "shortcode"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "custid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "rmsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "smsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageprice"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "product_type"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "product_code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "keyword"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_encoding"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "data_str"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_url"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "dnrep"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "group_tag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "remoteAdd"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "urltitle"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "ewigFlag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "cFlag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(Integer.TYPE);
        oper.setReturnQName(new QName("", "ReceiveExtMTPushReturn"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[3] = oper;
        oper = new OperationDesc();
        oper.setName("receiveExtMTPush");
        oper.addParameter(new QName("", "shortcode"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "custid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "rmsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "smsisdn"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageid"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "messageprice"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "product_type"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "product_code"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "keyword"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_encoding"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "data_str"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "data_url"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "dnrep"), new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, (byte)1, false, false);
        oper.addParameter(new QName("", "group_tag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "remoteAdd"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.addParameter(new QName("", "cFlag"), new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, (byte)1, false, false);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(Integer.TYPE);
        oper.setReturnQName(new QName("", "ReceiveExtMTPushReturn"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[4] = oper;
    }

    public ExternalMTPushInterfaceSoapBindingStub() throws AxisFault {
        this((Service)null);
    }

    public ExternalMTPushInterfaceSoapBindingStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public ExternalMTPushInterfaceSoapBindingStub(Service service) throws AxisFault {
        this.cachedSerClasses = new Vector();
        this.cachedSerQNames = new Vector();
        this.cachedSerFactories = new Vector();
        this.cachedDeserFactories = new Vector();
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }

    }

    private Call createCall() throws RemoteException {
        try {
            Call _call = (Call)super.service.createCall();
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

            return _call;
        } catch (Throwable t) {
            throw new AxisFault("Failure trying to get the Call object", t);
        }
    }

    public String HLRLookup(String shortcode, String custid, String rmsisdn, String remoteAdd, String outputType, int invokeLive) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[0]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("http://webservice.smsserver.isentric.com", "HLRLookup"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);
            Object _resp = _call.invoke(new Object[]{shortcode, custid, rmsisdn, remoteAdd, outputType, Integer.valueOf(invokeLive)});
            if (_resp instanceof RemoteException) {
                throw (RemoteException)_resp;
            } else {
                this.extractAttachments(_call);

                try {
                    return (String)_resp;
                } catch (Exception var10) {
                    return (String)JavaUtils.convert(_resp, String.class);
                }
            }
        }
    }

    public int receiveExtMTPush(String shortcode, String custid, String rmsisdn, String smsisdn, String messageid, String messageprice, int product_type, String product_code, String keyword, int data_encoding, String data_str, String data_url, int dnrep, String group_tag, String remoteAdd, String urltitle, String ewigFlag) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[1]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("http://webservice.smsserver.isentric.com", "receiveExtMTPush"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);
            Object _resp = _call.invoke(new Object[]{shortcode, custid, rmsisdn, smsisdn, messageid, messageprice, Integer.valueOf(product_type), product_code, keyword, Integer.valueOf(data_encoding), data_str, data_url, Integer.valueOf(dnrep), group_tag, remoteAdd, urltitle, ewigFlag});
            if (_resp instanceof RemoteException) {
                throw (RemoteException)_resp;
            } else {
                this.extractAttachments(_call);

                try {
                    return (Integer)_resp;
                } catch (Exception var21) {
                    return (Integer)JavaUtils.convert(_resp, Integer.TYPE);
                }
            }
        }
    }

    public int receiveExtMTPush(String shortcode, String custid, String rmsisdn, String smsisdn, String messageid, String messageprice, int product_type, String product_code, String keyword, int data_encoding, String data_str, String data_url, int dnrep, String group_tag, String remoteAdd) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[2]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("http://webservice.smsserver.isentric.com", "receiveExtMTPush"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);
            Object _resp = _call.invoke(new Object[]{shortcode, custid, rmsisdn, smsisdn, messageid, messageprice, Integer.valueOf(product_type), product_code, keyword, Integer.valueOf(data_encoding), data_str, data_url, Integer.valueOf(dnrep), group_tag, remoteAdd});
            if (_resp instanceof RemoteException) {
                throw (RemoteException)_resp;
            } else {
                this.extractAttachments(_call);

                try {
                    return (Integer)_resp;
                } catch (Exception var19) {
                    return (Integer)JavaUtils.convert(_resp, Integer.TYPE);
                }
            }
        }
    }

    public int receiveExtMTPush(String shortcode, String custid, String rmsisdn, String smsisdn, String messageid, String messageprice, int product_type, String product_code, String keyword, int data_encoding, String data_str, String data_url, int dnrep, String group_tag, String remoteAdd, String urltitle, String ewigFlag, String cFlag) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[3]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("http://webservice.smsserver.isentric.com", "receiveExtMTPush"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);
            Object _resp = _call.invoke(new Object[]{shortcode, custid, rmsisdn, smsisdn, messageid, messageprice, Integer.valueOf(product_type), product_code, keyword, Integer.valueOf(data_encoding), data_str, data_url, Integer.valueOf(dnrep), group_tag, remoteAdd, urltitle, ewigFlag, cFlag});
            if (_resp instanceof RemoteException) {
                throw (RemoteException)_resp;
            } else {
                this.extractAttachments(_call);

                try {
                    return (Integer)_resp;
                } catch (Exception var22) {
                    return (Integer)JavaUtils.convert(_resp, Integer.TYPE);
                }
            }
        }
    }

    public int receiveExtMTPush(String shortcode, String custid, String rmsisdn, String smsisdn, String messageid, String messageprice, int product_type, String product_code, String keyword, int data_encoding, String data_str, String data_url, int dnrep, String group_tag, String remoteAdd, String cFlag) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[4]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("http://webservice.smsserver.isentric.com", "receiveExtMTPush"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);
            Object _resp = _call.invoke(new Object[]{shortcode, custid, rmsisdn, smsisdn, messageid, messageprice, Integer.valueOf(product_type), product_code, keyword, Integer.valueOf(data_encoding), data_str, data_url, Integer.valueOf(dnrep), group_tag, remoteAdd, cFlag});
            if (_resp instanceof RemoteException) {
                throw (RemoteException)_resp;
            } else {
                this.extractAttachments(_call);

                try {
                    return (Integer)_resp;
                } catch (Exception var20) {
                    return (Integer)JavaUtils.convert(_resp, Integer.TYPE);
                }
            }
        }
    }
}
