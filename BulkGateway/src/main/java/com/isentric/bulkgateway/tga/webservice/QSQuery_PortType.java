//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.tga.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QSQuery_PortType extends Remote {
    QSResponse queryTGA(String var1) throws RemoteException;

    QSResponse queryTGASkipFilter(String var1) throws RemoteException;
}
