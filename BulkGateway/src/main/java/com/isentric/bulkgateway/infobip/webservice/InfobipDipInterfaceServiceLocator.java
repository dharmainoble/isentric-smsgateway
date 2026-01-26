//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.infobip.webservice;

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

public class InfobipDipInterfaceServiceLocator extends Service implements InfobipDipInterfaceService {
    private String Infobipws_address = "http://localhost:8010/axis/services/Infobipws";
    private String InfobipwsWSDDServiceName = "Infobipws";
    private HashSet ports = null;

    public InfobipDipInterfaceServiceLocator() {
    }

    public InfobipDipInterfaceServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public InfobipDipInterfaceServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    public String getInfobipwsAddress() {
        return this.Infobipws_address;
    }

    public String getInfobipwsWSDDServiceName() {
        return this.InfobipwsWSDDServiceName;
    }

    public void setInfobipwsWSDDServiceName(String name) {
        this.InfobipwsWSDDServiceName = name;
    }

    public InfobipDipInterface getInfobipws() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(this.Infobipws_address);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }

        return this.getInfobipws(endpoint);
    }

    public InfobipDipInterface getInfobipws(URL portAddress) throws ServiceException {
        try {
            InfobipwsSoapBindingStub _stub = new InfobipwsSoapBindingStub(portAddress, this);
            _stub.setPortName(this.getInfobipwsWSDDServiceName());
            return _stub;
        } catch (AxisFault var3) {
            return null;
        }
    }

    public void setInfobipwsEndpointAddress(String address) {
        this.Infobipws_address = address;
    }

    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (InfobipDipInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                InfobipwsSoapBindingStub _stub = new InfobipwsSoapBindingStub(new URL(this.Infobipws_address), this);
                _stub.setPortName(this.getInfobipwsWSDDServiceName());
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
            if ("Infobipws".equals(inputPortName)) {
                return this.getInfobipws();
            } else {
                Remote _stub = this.getPort(serviceEndpointInterface);
                ((Stub)_stub).setPortName(portName);
                return _stub;
            }
        }
    }

    public QName getServiceName() {
        return new QName("urn:webservice.Infobip.bg.isentric.com", "InfobipDipInterfaceService");
    }

    public Iterator getPorts() {
        if (this.ports == null) {
            this.ports = new HashSet();
            this.ports.add(new QName("urn:webservice.Infobip.bg.isentric.com", "Infobipws"));
        }

        return this.ports.iterator();
    }

    public void setEndpointAddress(String portName, String address) throws ServiceException {
        if ("Infobipws".equals(portName)) {
            this.setInfobipwsEndpointAddress(address);
        } else {
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        this.setEndpointAddress(portName.getLocalPart(), address);
    }
}
