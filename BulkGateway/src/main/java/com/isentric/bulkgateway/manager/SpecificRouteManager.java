//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.manager;

import com.isentric.bulkgateway.dto.SpecificRouteObject;
import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

public class SpecificRouteManager {
    private static SpecificRouteManager instance;
    private static int checkedOut = 0;
    // CacheAccess expects two type arguments: key and value
    private static CacheAccess<String, SpecificRouteObject> prefixCache;
    private static final Logger logger = LoggerManager.createLoggerPattern(SpecificRouteManager.class);

    private SpecificRouteManager() throws CacheException {
        prefixCache = JCS.getInstance("SpecificRouteCache");
    }

    public static SpecificRouteManager getInstance() throws CacheException {
        synchronized(SpecificRouteManager.class) {
            if (instance == null) {
                instance = new SpecificRouteManager();
            }
        }

        synchronized(instance) {
            SpecificRouteManager var10000 = instance;
            ++checkedOut;
        }

        return instance;
    }

    public SpecificRouteObject getPrefixObj(String key1, String key2) {
        return this.getPrefixObj(key1.toLowerCase(), key2.toLowerCase(), true);
    }

    public SpecificRouteObject getPrefixObj(String key1, String key2, boolean fromCache) {
        SpecificRouteObject vObj = new SpecificRouteObject();
        if (fromCache) {
            vObj = (SpecificRouteObject)prefixCache.get("Obj" + key1 + key2);
        }

        return vObj;
    }

    public void storeVObj(SpecificRouteObject vObj) throws CacheException {
        String key1 = vObj.getClient().toLowerCase();
        String key2 = vObj.getTelco().toLowerCase();
        if (key1.equals("") && key2.equals("")) {
            prefixCache.remove("Obj" + key1 + key2);
        }

        prefixCache.put("Obj" + key1 + key2, vObj);
    }
}
