//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.service.SmppMessageServiceBinder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class Ejb3Util {
    private static Hashtable<String, SmppMessageServiceBinder> contextHashtable = new Hashtable();

    public static SmppMessageServiceBinder getSmppMessageServiceBinder() throws NamingException {
        SmppMessageServiceBinder smppMessageServiceBinderInst = null;
        if (contextHashtable.containsKey("SmppMessageServiceBinderBean/local")) {
            smppMessageServiceBinderInst = (SmppMessageServiceBinder)contextHashtable.get("SmppMessageServiceBinderBean/local");
            if (smppMessageServiceBinderInst != null) {
                return smppMessageServiceBinderInst;
            }
        }

        InitialContext ctx = new InitialContext();
        smppMessageServiceBinderInst = (SmppMessageServiceBinder)ctx.lookup("SmppMessageServiceBinder/local");
        return smppMessageServiceBinderInst;
    }
}
