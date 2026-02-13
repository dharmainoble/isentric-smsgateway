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
            // Try direct access to ApplicationContext (safer and more reliable than getInstance wrapper)
            org.springframework.context.ApplicationContext ctx = com.isentric.bulkgateway.utility.ApplicationContextProvider.getContext();
            if (ctx != null) {
                try {
                    SmppMessageServiceBinder bean = ctx.getBean(SmppMessageServiceBinder.class);
                    if (bean != null) {
                        return bean;
                    }
                } catch (Exception ignored) {
                    // Bean not found in context - we'll create and autowire an instance below
                }

                // If the bean isn't defined in the context, create one and ask Spring to autowire its dependencies.
                try {
                    SmppMessageServiceBinder created = new SmppMessageServiceBinder();
                    try {
                        ctx.getAutowireCapableBeanFactory().autowireBean(created);
                        return created;
                    } catch (Throwable t) {
                        // Autowiring failed - fall through to other fallbacks
                    }
                } catch (Throwable t) {
                    // failed to create instance - fall through
                }
            }
        } catch (Throwable ignored) {
            // ignore and fallback to previous behaviour
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
                SmppMessageServiceBinder created = new SmppMessageServiceBinder();
                // If Spring context exists, try to autowire the newly created instance so @Autowired fields are populated
                try {
                    org.springframework.context.ApplicationContext ctx2 = com.isentric.bulkgateway.utility.ApplicationContextProvider.getContext();
                    if (ctx2 != null) {
                        try {
                          ctx2.getAutowireCapableBeanFactory().autowireBean(created);
                        } catch (Throwable t) {
                          // ignore autowire failures
                        }
                    }
                } catch (Throwable ignored) {
                }
                return created;
            } catch (Throwable t) {
                // rethrow original exception to preserve behaviour
                throw ne;
            }
        }
    }
}
