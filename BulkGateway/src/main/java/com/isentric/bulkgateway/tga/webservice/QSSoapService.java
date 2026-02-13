package com.isentric.bulkgateway.tga.webservice;

import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface QSSoapService {

        QSSoapServicePortType getQSSoapServiceSOAP11port_http(URL var1) throws ServiceException;

}
