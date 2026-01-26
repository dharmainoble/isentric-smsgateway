// 
// Decompiled by Procyon v0.5.32
// 

package com.isentric.bulkgateway.tga.webservice;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

public interface QSQueryService extends Service
{
    String getQSQueryAddress();
    
    QSQuery_PortType getQSQuery() throws ServiceException;
    
    QSQuery_PortType getQSQuery(final URL p0) throws ServiceException;
}
