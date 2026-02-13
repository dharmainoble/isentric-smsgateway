package com.isentric.bulkgateway.tga.webservice;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Service;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

public class QSSoapServiceLocator  extends Service implements QSSoapService {

    private String QSSoapServiceSOAP12port_http_address = "http://192.168.26.162:6669/QSWebApp/services/QSSoapService";
    private String QSSoapServiceSOAP12port_httpWSDDServiceName = "QSSoapServiceSOAP12port_http";
    private String QSSoapServiceSOAP11port_http_address = "http://192.168.26.162:6669/QSWebApp/services/QSSoapService";
    private String QSSoapServiceSOAP11port_httpWSDDServiceName = "QSSoapServiceSOAP11port_http";
    private HashSet ports = null;

    public QSSoapServiceLocator() {
    }


    public QSSoapServicePortType getQSSoapServiceSOAP11port_http(URL portAddress) {
        try {
            QSSoapServiceSOAP11BindingStub _stub = new QSSoapServiceSOAP11BindingStub(portAddress, this);
            _stub.setPortName(this.getQSSoapServiceSOAP11port_httpWSDDServiceName());
            return _stub;
        } catch (AxisFault var3) {
            return null;
        }
    }

    public String getQSSoapServiceSOAP11port_httpWSDDServiceName() {
        return this.QSSoapServiceSOAP11port_httpWSDDServiceName;
    }


}
