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
        // First try to obtain from Spring ApplicationContext if available
        try {
            com.isentric.bulkgateway.utility.ApplicationContextProvider ctxProvider = com.isentric.bulkgateway.utility.ApplicationContextProvider.getInstance();
            if (ctxProvider != null) {
                SmppMessageServiceBinder bean = ctxProvider.getBean(SmppMessageServiceBinder.class);
                if (bean != null) {
                    return bean;
                }
            }
        } catch (Throwable ignored) {
            // ignore and fallback to JNDI
        }

        if (contextHashtable.containsKey("SmppMessageServiceBinder/local")) {
            smppMessageServiceBinderInst = (SmppMessageServiceBinder)contextHashtable.get("SmppMessageServiceBinder/local");
            if (smppMessageServiceBinderInst != null) {
                return smppMessageServiceBinderInst;
            }
        }

        // try JNDI lookup (original behaviour)
        try {
            InitialContext ctx = new InitialContext();
            smppMessageServiceBinderInst = (SmppMessageServiceBinder)ctx.lookup("SmppMessageServiceBinder/local");
            return smppMessageServiceBinderInst;
        } catch (NamingException ne) {
            // If JNDI not available, fall back to creating a new instance (best-effort)
            try {
                return new SmppMessageServiceBinder();
            } catch (Throwable t) {
                // rethrow original exception to preserve behaviour
                throw ne;
            }
        }
    }
}
