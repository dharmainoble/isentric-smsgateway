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

public class MaxisDNStatusManager {
    private static MaxisDNStatusManager instance;
    private static int checkedOut = 0;
    private static CacheAccess<String, MaxisDNStatusObject> maxisDNStatusCache;
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
            java.util.List<?> rows;
            if (!key.equalsIgnoreCase("")) {
                sql = "select cpidentity, maxisdn_flag from bulk_config.cpip where cpidentity = ?";
                rows = com.isentric.bulkgateway.utility.EntityManagerFactoryProvider.executeNativeQueryAsArray("bulk_config", sql, key);
            } else {
                sql = "select cpidentity, maxisdn_flag from bulk_config.cpip";
                rows = com.isentric.bulkgateway.utility.EntityManagerFactoryProvider.executeNativeQueryAsArray("bulk_config", sql);
            }

            if (rows != null && !rows.isEmpty()) {
                for (Object row : rows) {
                    found = true;
                    if (row instanceof Object[]) {
                        Object[] cols = (Object[]) row;
                        // cols[0] = cpidentity, cols[1] = maxisdn_flag
                        vObj.setCpidentity(cols[0] != null ? String.valueOf(cols[0]) : "");
                        vObj.setMaxisdn_flag(cols[1] != null ? String.valueOf(cols[1]) : "");
                    } else {
                        // single column result fallback
                        vObj.setCpidentity(row != null ? String.valueOf(row) : "");
                    }
                }
            }

            if (found) {
                maxisDNStatusCache.remove("MaxisVObj" + key);
                maxisDNStatusCache.put("MaxisVObj" + key, vObj);
            }
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


}
