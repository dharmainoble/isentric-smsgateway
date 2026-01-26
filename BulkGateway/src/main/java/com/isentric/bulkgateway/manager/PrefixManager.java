//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.manager;


import com.isentric.bulkgateway.dto.Prefix;
import org.apache.jcs.JCS;
import org.apache.jcs.access.CacheAccess;
import org.apache.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

import java.util.List;

import com.isentric.bulkgateway.utility.EntityManagerFactoryProvider;

public class PrefixManager {
    private static PrefixManager instance;
    private static int checkedOut = 0;
    // CacheAccess expects key and value type parameters
    private static CacheAccess<String, Prefix> prefixCache;
    private static final Logger logger = LoggerManager.createLoggerPattern(PrefixManager.class);

    private PrefixManager() throws CacheException {
        // JCS.getInstance may return a raw CacheAccess; cast to our typed CacheAccess
        prefixCache = (CacheAccess<String, Prefix>) JCS.getInstance("prefixCache");
    }

    public static PrefixManager getInstance() throws CacheException {
        synchronized(PrefixManager.class) {
            if (instance == null) {
                instance = new PrefixManager();
            }
        }

        synchronized(instance) {
            PrefixManager var10000 = instance;
            ++checkedOut;
        }

        return instance;
    }

    public Prefix getPrefixObj(String key) {
        return this.getPrefixObj(key.toLowerCase(), true);
    }

    public Prefix getPrefixObj(String key, boolean fromCache) {
        Prefix vObj = null;
        if (fromCache) {
            vObj = (Prefix)prefixCache.get("PrefixVObj" + key);
        }

        if (vObj == null) {
            vObj = this.loadPrefixVObj(key);
        }

        if (vObj.getRoute().equals("")) {
            vObj = this.loadPrefixVObj(key);
        }

        return vObj;
    }

    public Prefix loadPrefixVObj(String key) {
        Prefix vObj = new Prefix();
        vObj.country = key;

        try {
            boolean found = false;
            String sql = "select route from bulk_config.credit_control where country = ?";
            List<Object[]> rows = EntityManagerFactoryProvider.executeNativeQueryAsArray("bulk_config", sql, key);

            if (rows != null && !rows.isEmpty()) {
                for (Object row : rows) {
                    found = true;
                    if (row instanceof Object[]) {
                        Object[] cols = (Object[]) row;
                        vObj.setRoute(cols[0] != null ? String.valueOf(cols[0]) : "");
                    } else {
                        // single column fallback
                        vObj.setRoute(row != null ? String.valueOf(row) : "");
                    }
                }
            }

            if (found) {
                prefixCache.remove("PrefixVObj" + key);
                prefixCache.put("PrefixVObj" + key, vObj);
            }
        } catch (Exception e) {
            logger.fatal(e);
        }

        return vObj;
    }

    public void storePrefixVObj(Prefix vObj) {
        try {
            String key = vObj.getCountry().toLowerCase();
            if (vObj.country != "") {
                prefixCache.remove("PrefixVObj" + key);
            }

            prefixCache.put("PrefixVObj" + key, vObj);
        } catch (Exception e) {
            logger.fatal(e);
        }

    }

    public void resetPrefixVObj(String country, String code) {
        try {
            prefixCache.remove("PrefixVObj" + country.toLowerCase());
            logger.info("Sucess to reset credit control for country [" + country + "] due to response code >>" + code);
        } catch (Exception e) {
            logger.info("Fail to reset credit control for country [" + country + "] due to response code >>" + code);
            logger.fatal(e);
        }

    }
}
