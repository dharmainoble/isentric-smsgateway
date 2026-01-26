//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.extmt.webservice;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface ExternalMTPushInterfaceService extends Service {
    String getExternalMTPushInterfaceAddress();

    ExternalMTPushInterface getExternalMTPushInterface() throws ServiceException;

    ExternalMTPushInterface getExternalMTPushInterface(URL var1) throws ServiceException;
}
