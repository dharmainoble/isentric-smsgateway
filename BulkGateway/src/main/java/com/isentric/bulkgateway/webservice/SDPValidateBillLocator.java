//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

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

public class SDPValidateBillLocator extends Service implements SDPValidateBill {
    private String SDPValidateBillServicesHttpPort_address = "http://192.100.86.204:8001/billing/services/SDPValidateBill";
    private String SDPValidateBillServicesHttpPortWSDDServiceName = "SDPValidateBillServicesHttpPort";
    private HashSet ports = null;

    public SDPValidateBillLocator() {
    }

    public SDPValidateBillLocator(EngineConfiguration config) {
        super(config);
    }

    public SDPValidateBillLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    public String getSDPValidateBillServicesHttpPortAddress() {
        return this.SDPValidateBillServicesHttpPort_address;
    }

    public String getSDPValidateBillServicesHttpPortWSDDServiceName() {
        return this.SDPValidateBillServicesHttpPortWSDDServiceName;
    }

    public void setSDPValidateBillServicesHttpPortWSDDServiceName(String name) {
        this.SDPValidateBillServicesHttpPortWSDDServiceName = name;
    }

    public SDPValidateBillServicesInterface getSDPValidateBillServicesHttpPort() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(this.SDPValidateBillServicesHttpPort_address);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }

        return this.getSDPValidateBillServicesHttpPort(endpoint);
    }

    public SDPValidateBillServicesInterface getSDPValidateBillServicesHttpPort(URL portAddress) throws ServiceException {
        try {
            SDPValidateBillServicesHttpBindingStub _stub = new SDPValidateBillServicesHttpBindingStub(portAddress, this);
            _stub.setPortName(this.getSDPValidateBillServicesHttpPortWSDDServiceName());
            return _stub;
        } catch (AxisFault var3) {
            return null;
        }
    }

    public void setSDPValidateBillServicesHttpPortEndpointAddress(String address) {
        this.SDPValidateBillServicesHttpPort_address = address;
    }

    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (SDPValidateBillServicesInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                SDPValidateBillServicesHttpBindingStub _stub = new SDPValidateBillServicesHttpBindingStub(new URL(this.SDPValidateBillServicesHttpPort_address), this);
                _stub.setPortName(this.getSDPValidateBillServicesHttpPortWSDDServiceName());
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
            if ("SDPValidateBillServicesHttpPort".equals(inputPortName)) {
                return this.getSDPValidateBillServicesHttpPort();
            } else {
                Remote _stub = this.getPort(serviceEndpointInterface);
                ((Stub)_stub).setPortName(portName);
                return _stub;
            }
        }
    }

    public QName getServiceName() {
        return new QName("http://xsd.gateway.sdp.digi.com", "SDPValidateBill");
    }

    public Iterator getPorts() {
        if (this.ports == null) {
            this.ports = new HashSet();
            this.ports.add(new QName("http://xsd.gateway.sdp.digi.com", "SDPValidateBillServicesHttpPort"));
        }

        return this.ports.iterator();
    }

    public void setEndpointAddress(String portName, String address) throws ServiceException {
        if ("SDPValidateBillServicesHttpPort".equals(portName)) {
            this.setSDPValidateBillServicesHttpPortEndpointAddress(address);
        } else {
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        this.setEndpointAddress(portName.getLocalPart(), address);
    }
}
