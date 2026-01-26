//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.extmt.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ExternalMTPushInterface extends Remote {
    String HLRLookup(String var1, String var2, String var3, String var4, String var5, int var6) throws RemoteException;

    int receiveExtMTPush(String var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8, String var9, int var10, String var11, String var12, int var13, String var14, String var15, String var16, String var17) throws RemoteException;

    int receiveExtMTPush(String var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8, String var9, int var10, String var11, String var12, int var13, String var14, String var15) throws RemoteException;

    int receiveExtMTPush(String var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8, String var9, int var10, String var11, String var12, int var13, String var14, String var15, String var16, String var17, String var18) throws RemoteException;

    int receiveExtMTPush(String var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8, String var9, int var10, String var11, String var12, int var13, String var14, String var15, String var16) throws RemoteException;
}
