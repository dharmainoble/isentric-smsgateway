//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.Dao;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

public class SDPServicesLocator extends Service implements SDPServices {
    private String SDPServicesHttpPort_address = "http://192.100.86.201:8001/cxf/services/SDPServices";
    private String SDPServicesHttpPortWSDDServiceName = "SDPServicesHttpPort";
    private HashSet ports = null;

    public SDPServicesLocator() {
    }

    public SDPServicesLocator(EngineConfiguration config) {
        super(config);
    }

    public SDPServicesLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    public String getSDPServicesHttpPortAddress() {
        return this.SDPServicesHttpPort_address;
    }

    public String getSDPServicesHttpPortWSDDServiceName() {
        return this.SDPServicesHttpPortWSDDServiceName;
    }

    public void setSDPServicesHttpPortWSDDServiceName(String name) {
        this.SDPServicesHttpPortWSDDServiceName = name;
    }

    public SDPServicesInterface getSDPServicesHttpPort() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(this.SDPServicesHttpPort_address);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }

        return this.getSDPServicesHttpPort(endpoint);
    }

    public SDPServicesInterface getSDPServicesHttpPort(URL portAddress) throws ServiceException {
        try {
            SDPServicesHttpBindingStub _stub = new SDPServicesHttpBindingStub(portAddress, this);
            _stub.setPortName(this.getSDPServicesHttpPortWSDDServiceName());
            return _stub;
        } catch (AxisFault var3) {
            return null;
        }
    }

    public void setSDPServicesHttpPortEndpointAddress(String address) {
        this.SDPServicesHttpPort_address = address;
    }

    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (SDPServicesInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                SDPServicesHttpBindingStub _stub = new SDPServicesHttpBindingStub(new URL(this.SDPServicesHttpPort_address), this);
                _stub.setPortName(this.getSDPServicesHttpPortWSDDServiceName());
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
            if ("SDPServicesHttpPort".equals(inputPortName)) {
                return this.getSDPServicesHttpPort();
            } else {
                Remote _stub = this.getPort(serviceEndpointInterface);
                ((Stub)_stub).setPortName(portName);
                return _stub;
            }
        }
    }

    public QName getServiceName() {
        return new QName("http://xsd.gateway.sdp.digi.com", "SDPServices");
    }

    public Iterator getPorts() {
        if (this.ports == null) {
            this.ports = new HashSet();
            this.ports.add(new QName("http://xsd.gateway.sdp.digi.com", "SDPServicesHttpPort"));
        }

        return this.ports.iterator();
    }

    public void setEndpointAddress(String portName, String address) throws ServiceException {
        if ("SDPServicesHttpPort".equals(portName)) {
            this.setSDPServicesHttpPortEndpointAddress(address);
        } else {
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        this.setEndpointAddress(portName.getLocalPart(), address);
    }
}
