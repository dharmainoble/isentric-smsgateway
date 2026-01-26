//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.infobip.webservice;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface InfobipDipInterfaceService extends Service {
    String getInfobipwsAddress();

    InfobipDipInterface getInfobipws() throws ServiceException;

    InfobipDipInterface getInfobipws(URL var1) throws ServiceException;
}
