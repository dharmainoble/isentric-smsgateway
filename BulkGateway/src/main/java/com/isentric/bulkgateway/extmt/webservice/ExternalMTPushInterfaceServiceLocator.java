//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.extmt.webservice;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

public class ExternalMTPushInterfaceServiceLocator extends Service implements ExternalMTPushInterfaceService {
    private final String ExternalMTPushInterface_address = "http://203.223.130.115:8001/ExtMTPush/services/ExternalMTPushInterface";
    private String ExternalMTPushInterfaceWSDDServiceName = "ExternalMTPushInterface";
    private HashSet ports = null;

    public String getExternalMTPushInterfaceAddress() {
        return "http://203.223.130.115:8001/ExtMTPush/services/ExternalMTPushInterface";
    }

    public String getExternalMTPushInterfaceWSDDServiceName() {
        return this.ExternalMTPushInterfaceWSDDServiceName;
    }

    public void setExternalMTPushInterfaceWSDDServiceName(String name) {
        this.ExternalMTPushInterfaceWSDDServiceName = name;
    }

    public ExternalMTPushInterface getExternalMTPushInterface() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL("http://203.223.130.115:8001/ExtMTPush/services/ExternalMTPushInterface");
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }

        return this.getExternalMTPushInterface(endpoint);
    }

    public ExternalMTPushInterface getExternalMTPushInterface(URL portAddress) throws ServiceException {
        try {
            ExternalMTPushInterfaceSoapBindingStub _stub = new ExternalMTPushInterfaceSoapBindingStub(portAddress, this);
            _stub.setPortName(this.getExternalMTPushInterfaceWSDDServiceName());
            return _stub;
        } catch (AxisFault var3) {
            return null;
        }
    }

    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (ExternalMTPushInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                ExternalMTPushInterfaceSoapBindingStub _stub = new ExternalMTPushInterfaceSoapBindingStub(new URL("http://203.223.130.115:8001/ExtMTPush/services/ExternalMTPushInterface"), this);
                _stub.setPortName(this.getExternalMTPushInterfaceWSDDServiceName());
                return _stub;
            }
        } catch (Throwable t) {
            throw new ServiceException(t);
        }

        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if (portName == null) {
            return this.getPort(serviceEndpointInterface);
        } else {
            String inputPortName = portName.getLocalPart();
            if ("ExternalMTPushInterface".equals(inputPortName)) {
                return this.getExternalMTPushInterface();
            } else {
                Remote _stub = this.getPort(serviceEndpointInterface);
                ((Stub)_stub).setPortName(portName);
                return _stub;
            }
        }
    }

    public QName getServiceName() {
        return new QName("http://webservice.extmt.bg.isentric.com", "ExternalMTPushInterfaceService");
    }

    public Iterator getPorts() {
        if (this.ports == null) {
            this.ports = new HashSet();
            this.ports.add(new QName("ExternalMTPushInterface"));
        }

        return this.ports.iterator();
    }
}
