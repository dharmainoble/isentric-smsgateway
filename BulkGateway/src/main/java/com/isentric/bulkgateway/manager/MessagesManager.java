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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MessagesManager {
    private static MessagesManager instance;
    private static int checkedOut = 0;
    private static CacheAccess<Object> messagesCache;
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
            if (!key.equalsIgnoreCase("")) {
                sql = "select custid,messages from bulk_config.specific_messages where status = '0' and custid ='" + key + "'";
            } else {
                sql = "select custid,messages from bulk_config.specific_messages where status = '0' ";
            }

            DBManager.setupDriver();
            DBManagerDs dbManager = DBManagerDs.getManager("avatar");
            Connection con = dbManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs;
            for(rs = stmt.executeQuery(sql); rs.next(); found = true) {
                vObj.setMessages(rs.getString("custid"));
                vObj.setMessages(rs.getString("messages"));
            }

            if (found) {
                messagesCache.remove("MessagesVObj" + key);
                messagesCache.put("MessagesVObj" + key, vObj);
            }

            rs.close();
            stmt.close();
            dbManager.freeConnection(con);
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

    public void resetMessagesVObj() {
        try {
            messagesCache.remove();
            instance = null;
            this.loadMessagesVObj("");
            logger.info("Sucess to reset Messages ");
        } catch (Exception e) {
            logger.info("Fail to reset Messages ");
            logger.fatal(e);
        }

    }
}
