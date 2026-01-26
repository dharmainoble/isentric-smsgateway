//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.manager;


import com.isentric.bulkgateway.dto.SpecificMessagesObject;
import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

import java.util.List;

import com.isentric.bulkgateway.utility.EntityManagerFactoryProvider;

public class MessagesManager {
    private static MessagesManager instance;
    private static int checkedOut = 0;
    // CacheAccess expects two type arguments: key and value
    private static CacheAccess<String, Object> messagesCache;
    private static final Logger logger = LoggerManager.createLoggerPattern(MessagesManager.class);

    private MessagesManager() throws CacheException {
        messagesCache = JCS.getInstance("messagesCache");
    }

    public static MessagesManager getInstance() throws CacheException {
        synchronized(MessagesManager.class) {
            if (instance == null) {
                instance = new MessagesManager();
            }
        }

        synchronized(instance) {
            MessagesManager var10000 = instance;
            ++checkedOut;
        }

        return instance;
    }

    public SpecificMessagesObject getMessagesObj(String key) {
        return this.getMessagesObj(key.toLowerCase(), true);
    }

    public SpecificMessagesObject getMessagesObj(String key, boolean fromCache) {
        SpecificMessagesObject vObj = null;
        if (fromCache) {
            vObj = (SpecificMessagesObject)messagesCache.get("MessagesVObj" + key);
        }

        return vObj;
    }

    public SpecificMessagesObject loadMessagesVObj(String key) {
        SpecificMessagesObject vObj = new SpecificMessagesObject();
        vObj.custid = key;

        try {
            boolean found = false;
            String sql;
            List<?> rows;
            if (!key.equalsIgnoreCase("")) {
                sql = "select custid,messages from bulk_config.specific_messages where status = '0' and custid = ?";
                rows = EntityManagerFactoryProvider.executeNativeQuery("bulk_config", sql, key);
            } else {
                sql = "select custid,messages from bulk_config.specific_messages where status = '0' ";
                rows = EntityManagerFactoryProvider.executeNativeQuery("bulk_config", sql);
            }

            // Use JPA EntityManager provider to execute the native SQL and obtain results
            if (rows != null && !rows.isEmpty()) {
                for (Object row : rows) {
                    found = true;
                    if (row instanceof Object[]) {
                        Object[] cols = (Object[]) row;
                        // expect cols[0] = custid, cols[1] = messages
                        vObj.setCustid(cols[0] != null ? String.valueOf(cols[0]) : "");
                        vObj.setMessages(cols[1] != null ? String.valueOf(cols[1]) : "");
                    } else {
                        // single column - treat as messages
                        vObj.setMessages(row != null ? String.valueOf(row) : "");
                    }
                }
            }

            if (found) {
                messagesCache.remove("MessagesVObj" + key);
                messagesCache.put("MessagesVObj" + key, vObj);
            }

        } catch (Exception e) {
            logger.fatal(e);
        }

        return vObj;
    }

    public void storeMessagesVObj(SpecificMessagesObject vObj) {
        try {
            String key = vObj.getCustid().toLowerCase();
            if (vObj.custid != "") {
                messagesCache.remove("MessagesVObj" + key);
            }

            messagesCache.put("MessagesVObj" + key, vObj);
        } catch (Exception e) {
            logger.fatal(e);
        }

    }

    public void resetMessagesVObj(String custid) {
        try {
            messagesCache.remove("MessagesVObj" + custid.toLowerCase());
            instance = null;
            this.loadMessagesVObj(custid);
            logger.info("Sucess to reset Messages for custid [" + custid + "]");
        } catch (Exception e) {
            logger.info("Fail to reset Messages for custid [" + custid + "]");
            logger.fatal(e);
        }

    }


}
