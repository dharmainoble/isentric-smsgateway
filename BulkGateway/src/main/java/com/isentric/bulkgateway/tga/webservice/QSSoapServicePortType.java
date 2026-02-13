package com.isentric.bulkgateway.tga.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QSSoapServicePortType extends Remote {
    String queryRN(String var1, byte[] var2, String var3) throws RemoteException;
}

