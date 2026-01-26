//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.infobip.webservice;



import com.isentric.bulkgateway.tga.webservice.QSResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InfobipDipInterface extends Remote {
    QSResponse InfobipDipping(String var1) throws RemoteException;

    QSResponse InfobipDippingSkipFilter(String var1) throws RemoteException;
}
