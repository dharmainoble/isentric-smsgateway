package com.isentric.bulkgateway.tga.webservice;

import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

import javax.xml.namespace.QName;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;

public class QSSoapServiceSOAP11BindingStub extends Stub implements QSSoapServicePortType {
    private Vector cachedSerClasses;
    private Vector cachedSerQNames;
    private Vector cachedSerFactories;
    private Vector cachedDeserFactories;
    static OperationDesc[] _operations = new OperationDesc[2];



    private static void _initOperationDesc1() {
        OperationDesc oper = new OperationDesc();
        oper.setName("getVersion");
        oper.setReturnType(new QName("http://mnp.tga.com", ">getVersionResponse"));
        oper.setReturnQName(new QName("http://mnp.tga.com", "getVersionResponse"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[0] = oper;
        oper = new OperationDesc();
        oper.setName("queryRN");
        ParameterDesc param = new ParameterDesc(new QName("http://mnp.tga.com", "userId"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://mnp.tga.com", "password"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://mnp.tga.com", "xmlData"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("http://mnp.tga.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[1] = oper;
    }

    public QSSoapServiceSOAP11BindingStub() throws AxisFault {
        this((Service)null);
    }

    public QSSoapServiceSOAP11BindingStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public QSSoapServiceSOAP11BindingStub(Service service) throws AxisFault {
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

            return _call;
        } catch (Throwable _t) {
            throw new AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public String queryRN(String userId, byte[] password, String xmlData) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[1]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("urn:queryRN");
            _call.setEncodingStyle((String)null);
            _call.setProperty("sendXsiTypes", Boolean.FALSE);
            _call.setProperty("sendMultiRefs", Boolean.FALSE);
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("http://mnp.tga.com", "queryRN"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = null;
                _resp = _call.invoke(new Object[]{userId, password, xmlData});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (String)_resp;
                    } catch (Exception var8) {
                        return (String) JavaUtils.convert(_resp, String.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                String requestXML = _call.getMessageContext().getRequestMessage().getSOAPPartAsString();
                String responseXML = _call.getMessageContext().getResponseMessage().getSOAPPartAsString();
                System.out.println("-------- Request and Response XML ---------------");
                System.out.println(requestXML);
                System.out.println(responseXML);
                System.out.println("-------- Request and Response XML END---------------");
                throw axisFaultException;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
    }

    static {
        _initOperationDesc1();
    }
}
