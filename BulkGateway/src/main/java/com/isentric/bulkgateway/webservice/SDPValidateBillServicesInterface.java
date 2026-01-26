//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SDPValidateBillServicesInterface extends Remote {
    SDPResult bill(String var1, String var2, String var3, String var4, String var5, String var6, String var7, AdditionalInfo[] var8) throws RemoteException;

    SDPResult validate(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, int var11, String var12, VariableBundle[] var13, AdditionalInfo[] var14) throws RemoteException;

    SDPResult validateAndBill(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, int var11, String var12, VariableBundle[] var13, AdditionalInfo[] var14) throws RemoteException;

    SDPResult validate(String loginName, String serviceId, String cpId, String priceCode, String chargeParty, String sender, String sender1, String subId, String keyword, String deliveryChannel, int status, String refId, com.isentric.bulkgateway.dto.VariableBundle[] variableBundles, com.isentric.bulkgateway.dto.AdditionalInfo[] additionalInfos);
}
