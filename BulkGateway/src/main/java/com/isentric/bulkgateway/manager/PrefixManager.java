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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PrefixManager {
    private static PrefixManager instance;
    private static int checkedOut = 0;
    private static CacheAccess<Object> prefixCache;
    private static final Logger logger = LoggerManager.createLoggerPattern(PrefixManager.class);

    private PrefixManager() throws CacheException {
        prefixCache = JCS.getInstance("prefixCache");
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
            String sql = "select route from bulk_config.credit_control where country = '" + key + "'";
            DBManager.setupDriver();
            DBManagerDs dbManager = DBManagerDs.getManager("avatar");
            Connection con = dbManager.getConnection();
            Statement stmt = con.createStatement();

            ResultSet rs;
            for(rs = stmt.executeQuery(sql); rs.next(); found = true) {
                vObj.setRoute(rs.getString("route"));
            }

            if (found) {
                prefixCache.remove("PrefixVObj" + key);
                prefixCache.put("PrefixVObj" + key, vObj);
            }

            rs.close();
            stmt.close();
            dbManager.freeConnection(con);
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
