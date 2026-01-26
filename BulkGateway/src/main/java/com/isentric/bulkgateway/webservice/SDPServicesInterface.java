//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

public interface SDPServicesInterface extends Remote {
    VSDBQueryResult vsdbAdd(String var1, String var2, String var3, String var4, String var5, Calendar var6, String var7, AdditionalInfo[] var8) throws RemoteException;

    VSDBQueryResult vsdbRnw(String var1, String var2, String var3, String var4, String var5, String var6, Calendar var7, AdditionalInfo[] var8) throws RemoteException;

    VSDBQueryResult vsdbTerm(String var1, String var2, String var3, String var4, String var5, AdditionalInfo[] var6) throws RemoteException;

    VSDBQueryResult vsdbQuery(String var1, String var2, String var3, String var4, String var5, String var6, AdditionalInfo[] var7) throws RemoteException;

    SyncResult subQueryFile(String var1, String var2, String var3, AdditionalInfo[] var4) throws RemoteException;

    SubQueryResult subQuery(String var1, String var2, String var3, String var4, String var5, AdditionalInfo[] var6) throws RemoteException;

    SDPResult smsMt(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, int var13, String var14, String var15, String var16, String var17, int var18, String var19, SMSContent[] var20, AdditionalInfo[] var21) throws RemoteException;

    SDPResult smsBulk(String var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8, SMSContent[] var9, AdditionalInfo[] var10) throws RemoteException;

    SDPResult mmsMt(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, int var11, String var12, String var13, int var14, String var15, String var16, MMSContent[] var17, AttachmentType var18, AdditionalInfo[] var19) throws RemoteException;

    SDPResult mmsBulk(String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, String var9, MMSContent[] var10, AttachmentType var11, AdditionalInfo[] var12) throws RemoteException;

    SDPResult wappush(String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, String var9, String var10, int var11, String var12, WAPPushContent var13, AdditionalInfo[] var14) throws RemoteException;

    SDPResult wappushBulk(String var1, String var2, String var3, String var4, String var5, int var6, String var7, WAPPushContent var8, AdditionalInfo[] var9) throws RemoteException;

    SDPResult subRegMt(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, AdditionalInfo[] var10) throws RemoteException;

    SDPResult subRmdMt(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9, String var10, int var11, String var12, SMSContent[] var13) throws RemoteException;

    SDPResult subRnwMt(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9, String var10, int var11, String var12, String var13, SMSContent[] var14) throws RemoteException;

    SDPResult subSmsMt(String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, String var9, SMSContent[] var10, AdditionalInfo[] var11) throws RemoteException;

    SDPResult subMmsMt(String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, String var9, String var10, MMSContent[] var11, AttachmentType var12, AdditionalInfo[] var13) throws RemoteException;

    SDPResult subWappush(String var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8, WAPPushContent var9, AdditionalInfo[] var10) throws RemoteException;

    SDPResult subTermSvc(String var1, String var2, String var3, String var4, String var5, AdditionalInfo[] var6) throws RemoteException;

    SDPResult subBcSms(String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, String var9, Calendar var10, SMSContent[] var11, AdditionalInfo[] var12) throws RemoteException;

    SDPResult subBcMms(String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, String var9, Calendar var10, String var11, MMSContent[] var12, AttachmentType var13, AdditionalInfo[] var14) throws RemoteException;

    SDPResult subBcWappush(String var1, String var2, String var3, String var4, String var5, String var6, int var7, String var8, Calendar var9, WAPPushContent var10, AdditionalInfo[] var11) throws RemoteException;
}
