//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.tga.webservice;

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

public class QSQueryServiceLocator extends Service implements QSQueryService {
    private String QSQuery_address = "http://192.168.26.100:8001/TGA/services/QSQuery";
    private String QSQueryWSDDServiceName = "QSQuery";
    private HashSet ports = null;

    public QSQueryServiceLocator() {
    }

    public QSQueryServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public QSQueryServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    public String getQSQueryAddress() {
        return this.QSQuery_address;
    }

    public String getQSQueryWSDDServiceName() {
        return this.QSQueryWSDDServiceName;
    }

    public void setQSQueryWSDDServiceName(String name) {
        this.QSQueryWSDDServiceName = name;
    }

    public QSQuery_PortType getQSQuery() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(this.QSQuery_address);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }

        return this.getQSQuery(endpoint);
    }

    public QSQuery_PortType getQSQuery(URL portAddress) throws ServiceException {
        try {
            QSQuerySoapBindingStub _stub = new QSQuerySoapBindingStub(portAddress, this);
            _stub.setPortName(this.getQSQueryWSDDServiceName());
            return _stub;
        } catch (AxisFault var3) {
            return null;
        }
    }

    public void setQSQueryEndpointAddress(String address) {
        this.QSQuery_address = address;
    }

    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (QSQuery_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                QSQuerySoapBindingStub _stub = new QSQuerySoapBindingStub(new URL(this.QSQuery_address), this);
                _stub.setPortName(this.getQSQueryWSDDServiceName());
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
            if ("QSQuery".equals(inputPortName)) {
                return this.getQSQuery();
            } else {
                Remote _stub = this.getPort(serviceEndpointInterface);
                ((Stub)_stub).setPortName(portName);
                return _stub;
            }
        }
    }

    public QName getServiceName() {
        return new QName("http://webserviceinterface.qs.mnp.isentric.com", "QSQueryService");
    }

    public Iterator getPorts() {
        if (this.ports == null) {
            this.ports = new HashSet();
            this.ports.add(new QName("http://webserviceinterface.qs.mnp.isentric.com", "QSQuery"));
        }

        return this.ports.iterator();
    }

    public void setEndpointAddress(String portName, String address) throws ServiceException {
        if ("QSQuery".equals(portName)) {
            this.setQSQueryEndpointAddress(address);
        } else {
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        this.setEndpointAddress(portName.getLocalPart(), address);
    }
}
