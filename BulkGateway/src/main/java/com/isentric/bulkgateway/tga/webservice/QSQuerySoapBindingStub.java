//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.tga.webservice;

import com.isentric.bulkgateway.service.TGAService;
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

public class QSQuerySoapBindingStub extends Stub implements QSQuery_PortType {
    private Vector cachedSerClasses;
    private Vector cachedSerQNames;
    private Vector cachedSerFactories;
    private Vector cachedDeserFactories;
    static OperationDesc[] _operations = new OperationDesc[2];

    static {
        _initOperationDesc1();
    }

    private static void _initOperationDesc1() {
        OperationDesc oper = new OperationDesc();
        oper.setName("queryTGA");
        ParameterDesc param = new ParameterDesc(new QName("", "in0"), (byte)1, new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("urn:webservice.qs.mnp.isentric.com", "QSResponse"));
        oper.setReturnClass(QSResponse.class);
        oper.setReturnQName(new QName("", "queryTGAReturn"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[0] = oper;
        oper = new OperationDesc();
        oper.setName("queryTGASkipFilter");
        param = new ParameterDesc(new QName("", "in0"), (byte)1, new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("urn:webservice.qs.mnp.isentric.com", "QSResponse"));
        oper.setReturnClass(QSResponse.class);
        oper.setReturnQName(new QName("", "queryTGASkipFilterReturn"));
        oper.setStyle(Style.RPC);
        oper.setUse(Use.ENCODED);
        _operations[1] = oper;
    }

    public QSQuerySoapBindingStub() throws AxisFault {
        this((Service)null);
    }

    public QSQuerySoapBindingStub(URL endpointURL, Service service) throws AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public QSQuerySoapBindingStub(Service service) throws AxisFault {
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
        QName qName = new QName("urn:webservice.qs.mnp.isentric.com", "QSResponse");
        this.cachedSerQNames.add(qName);
        Class cls = QSResponse.class;
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
                    _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

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


    TGAService service;
    public QSResponse queryTGA(String in0) throws RemoteException {
       return service.queryTGA(in0);
    }

    public QSResponse queryTGASkipFilter(String in0) throws RemoteException {
        return service.queryTGASkipFilter(in0);
    }


    /*public QSResponse queryTGA(String in0) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[0]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("urn:webservice.qs.mnp.isentric.com", "queryTGA"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{in0});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (QSResponse)_resp;
                    } catch (Exception var5) {
                        return (QSResponse)JavaUtils.convert(_resp, QSResponse.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }*/

    /*public QSResponse queryTGASkipFilter(String in0) throws RemoteException {
        if (super.cachedEndpoint == null) {
            throw new NoEndPointException();
        } else {
            Call _call = this.createCall();
            _call.setOperation(_operations[1]);
            _call.setUseSOAPAction(true);
            _call.setSOAPActionURI("");
            _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
            _call.setOperationName(new QName("urn:webservice.qs.mnp.isentric.com", "queryTGASkipFilter"));
            this.setRequestHeaders(_call);
            this.setAttachments(_call);

            try {
                Object _resp = _call.invoke(new Object[]{in0});
                if (_resp instanceof RemoteException) {
                    throw (RemoteException)_resp;
                } else {
                    this.extractAttachments(_call);

                    try {
                        return (QSResponse)_resp;
                    } catch (Exception var5) {
                        return (QSResponse)JavaUtils.convert(_resp, QSResponse.class);
                    }
                }
            } catch (AxisFault axisFaultException) {
                throw axisFaultException;
            }
        }
    }*/
}
