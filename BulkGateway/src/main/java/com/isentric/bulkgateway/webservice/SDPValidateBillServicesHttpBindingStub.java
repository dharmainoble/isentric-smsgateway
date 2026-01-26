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
import java.util.Enumeration;
import java.util.Vector;

public class SDPValidateBillServicesHttpBindingStub extends Stub implements SDPValidateBillServicesInterface {
    private Vector cachedSerClasses;
    private Vector cachedSerQNames;
    private Vector cachedSerFactories;
    private Vector cachedDeserFactories;
    static OperationDesc[] _operations = new OperationDesc[3];

    private static void _initOperationDesc1() {
        OperationDesc oper = new OperationDesc();
        oper.setName("Bill");
        ParameterDesc param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "login_name"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "service_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "cp_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "destination_mobtel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "delivery_channel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "transaction_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
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
        _operations[0] = oper;
        oper = new OperationDesc();
        oper.setName("Validate");
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
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "delivery_channel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "variable_bundles"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfVariableBundle"), VariableBundle[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "variable_bundle"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[1] = oper;
        oper = new OperationDesc();
        oper.setName("ValidateAndBill");
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
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "keyword"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "delivery_channel"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "status"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "int"), Integer.TYPE, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "ref_id"), (byte)1, new QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "variable_bundles"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfVariableBundle"), VariableBundle[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "variable_bundle"));
        oper.addParameter(param);
        param = new ParameterDesc(new QName("http://xsd.gateway.sdp.digi.com", "array_of_info"), (byte)1, new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfAdditionalInfo"), AdditionalInfo[].class, false, false);
        param.setItemQName(new QName("http://xsd.gateway.sdp.digi.com", "additional_info"));
        oper.addParameter(param);
        oper.setReturnType(new QName("http://xsd.gateway.sdp.digi.com", "SDPResult"));
        oper.setReturnClass(SDPResult.class);
        oper.setReturnQName(new QName("http://xsd.gateway.sdp.digi.com", "return"));
        oper.setStyle(Style.WRAPPED);
        oper.setUse(Use.LITERAL);
        _operations[2] = oper;
    }

    public SDPValidateBillServicesHttpBindingStub() throws AxisFault {
        this((Service)null);
    }

    public SDPValidateBillServicesHttpBindingStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public SDPValidateBillServicesHttpBindingStub(Service service) throws AxisFault {
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
        qName = new QName("http://xsd.gateway.sdp.digi.com", "ArrayOfVariableBundle");
        this.cachedSerQNames.add(qName);
        cls = VariableBundle[].class;
        this.cachedSerClasses.add(cls);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "VariableBundle");
        qName2 = new QName("http://xsd.gateway.sdp.digi.com", "variable_bundle");
        this.cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
        this.cachedDeserFactories.add(new ArrayDeserializerFactory());
        qName = new QName("http://xsd.gateway.sdp.digi.com", "SDPResult");
        this.cachedSerQNames.add(qName);
        cls = SDPResult.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
        qName = new QName("http://xsd.gateway.sdp.digi.com", "VariableBundle");
        this.cachedSerQNames.add(qName);
        cls = VariableBundle.class;
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

    public SDPResult bill(String login_name, String service_id, String cp_id, String destination_mobtel, String delivery_channel, String transaction_id, String ref_id, AdditionalInfo[] array_of_info) throws RemoteException {
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
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "Bill"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, destination_mobtel, delivery_channel, transaction_id, ref_id, array_of_info});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (SDPResult)_resp;
                    } catch (Exception var12) {
                        return (SDPResult)JavaUtils.convert(_resp, SDPResult.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }

    public SDPResult validate(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String keyword, String delivery_channel, int status, String ref_id, VariableBundle[] variable_bundles, AdditionalInfo[] array_of_info) throws RemoteException {
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
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "Validate"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, price_code, charge_party, source_mobtel, destination_mobtel, sub_id, keyword, delivery_channel, Integer.valueOf(status), ref_id, variable_bundles, array_of_info});
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

    public SDPResult validateAndBill(String login_name, String service_id, String cp_id, String price_code, String charge_party, String source_mobtel, String destination_mobtel, String sub_id, String keyword, String delivery_channel, int status, String ref_id, VariableBundle[] variable_bundles, AdditionalInfo[] array_of_info) throws RemoteException {
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
            _call.setOperationName(new QName("http://xsd.gateway.sdp.digi.com", "ValidateAndBill"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{login_name, service_id, cp_id, price_code, charge_party, source_mobtel, destination_mobtel, sub_id, keyword, delivery_channel, Integer.valueOf(status), ref_id, variable_bundles, array_of_info});
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

    public SDPResult validate1(String loginName, String serviceId, String cpId, String priceCode, String chargeParty, String sender, String sender1, String subId, String keyword, String deliveryChannel, int status, String refId, VariableBundle[] variableBundles, AdditionalInfo[] additionalInfos) {
        try {
            return this.validate(loginName, serviceId, cpId, priceCode, chargeParty, sender, sender1, subId, keyword, deliveryChannel, status, refId, variableBundles, additionalInfos);
        } catch (RemoteException e) {
            // Interface does not declare RemoteException, wrap in unchecked
            throw new RuntimeException("Remote call failed", e);
        }
    }

    static {
        _initOperationDesc1();
    }
}
