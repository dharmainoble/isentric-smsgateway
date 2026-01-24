//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.manager;


import com.isentric.bulkgateway.dto.MaxisDNStatusObject;
import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MaxisDNStatusManager {
    private static MaxisDNStatusManager instance;
    private static int checkedOut = 0;
    private static CacheAccess<Object> maxisDNStatusCache;
    private static final Logger logger = LoggerManager.createLoggerPattern(MaxisDNStatusManager.class);

    private MaxisDNStatusManager() throws CacheException {
        maxisDNStatusCache = JCS.getInstance("maxisDNStatusCache");
    }

    public static MaxisDNStatusManager getInstance() throws CacheException {
        synchronized(MaxisDNStatusManager.class) {
            if (instance == null) {
                instance = new MaxisDNStatusManager();
            }
        }

        synchronized(instance) {
            ++checkedOut;
        }

        return instance;
    }

    public MaxisDNStatusObject getMessagesObj(String key) {
        return this.getMaxisDNStatusObj(key.toLowerCase(), true);
    }

    public MaxisDNStatusObject getMaxisDNStatusObj(String key, boolean fromCache) {
        MaxisDNStatusObject vObj = null;
        if (fromCache) {
            vObj = (MaxisDNStatusObject)maxisDNStatusCache.get("MaxisVObj" + key);
        }

        return vObj;
    }

    public MaxisDNStatusObject loadMaxisVObj(String key) {
        MaxisDNStatusObject vObj = new MaxisDNStatusObject();
        vObj.cpidentity = key;

        try {
            boolean found = false;
            String sql;
            if (!key.equalsIgnoreCase("")) {
                sql = "select cpidentity,maxisdn_flag from bulk_config.cpip where cpidentity ='" + key + "'";
            } else {
                sql = "select cpidentity,maxisdn_flag from bulk_config.cpip";
            }

            DBManager.setupDriver();
            DBManagerDs dbManager = DBManagerDs.getManager("avatar");
            Connection con = dbManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs;
            for(rs = stmt.executeQuery(sql); rs.next(); found = true) {
                vObj.setCpidentity(rs.getString("cpidentity"));
                vObj.setMaxisdn_flag(rs.getString("maxisdn_flag"));
            }

            if (found) {
                maxisDNStatusCache.remove("MaxisVObj" + key);
                maxisDNStatusCache.put("MaxisVObj" + key, vObj);
            }

            rs.close();
            stmt.close();
            dbManager.freeConnection(con);
        } catch (Exception e) {
            logger.fatal(e);
        }

        return vObj;
    }

    public void storeMaxisVObj(MaxisDNStatusObject vObj) {
        try {
            String key = vObj.getCpidentity().toLowerCase();
            logger.info("===================key=================" + key);
            if (vObj.cpidentity != "") {
                maxisDNStatusCache.remove("MaxisVObj" + key);
            }

            maxisDNStatusCache.put("MaxisVObj" + key, vObj);
        } catch (Exception e) {
            logger.fatal(e);
        }

    }

    public void resetMessagesVObj(String cpidentity) {
        try {
            maxisDNStatusCache.remove("MaxisVObj" + cpidentity.toLowerCase());
            instance = null;
            this.loadMaxisVObj(cpidentity);
            logger.info("Sucess to reset Maxis DN Status for cpidentity [" + cpidentity + "]");
        } catch (Exception e) {
            logger.info("Fail to reset Maxis DN Status for cpidentity [" + cpidentity + "]");
            logger.fatal(e);
        }

    }

    public void resetMaxisVObj() {
        try {
            maxisDNStatusCache.remove();
            instance = null;
            this.loadMaxisVObj("");
            logger.info("Sucess to reset Maxis DN Status ");
        } catch (Exception e) {
            logger.info("Fail to reset Maxis DN Status ");
            logger.fatal(e);
        }

    }
}
