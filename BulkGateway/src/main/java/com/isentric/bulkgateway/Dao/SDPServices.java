//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.Dao;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface SDPServices extends Service {
    String getSDPServicesHttpPortAddress();

    SDPServicesInterface getSDPServicesHttpPort() throws ServiceException;

    SDPServicesInterface getSDPServicesHttpPort(URL var1) throws ServiceException;
}
