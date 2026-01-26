//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface SDPValidateBill extends Service {
    String getSDPValidateBillServicesHttpPortAddress();

    SDPValidateBillServicesInterface getSDPValidateBillServicesHttpPort() throws ServiceException;

    SDPValidateBillServicesInterface getSDPValidateBillServicesHttpPort(URL var1) throws ServiceException;
}
