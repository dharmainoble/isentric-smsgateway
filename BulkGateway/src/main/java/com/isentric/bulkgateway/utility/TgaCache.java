//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

import com.isentric.bulkgateway.dto.tgaObject;
import com.isentric.bulkgateway.manager.LoggerManager;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TgaCache {
    private static final Logger logger = LoggerManager.createLoggerPattern(TgaCache.class);
    TagDao dao = new TagDao();
    private static TgaCache sharedObj = null;
    private static ArrayList<tgaObject> list = null;

    public TgaCache() throws NamingException, SQLException {
        String sql = "select custid, filter_flag, server, dip_engine from bulk_config.specific_tga ";
        logger.info("TgaCache init()");

        try {
            List<Object> result = this.dao.query(sql);
            // ensure list is always initialized
            list = new ArrayList<>();

            if (result != null && !result.isEmpty()) {

                for (Object element : result) {
                    if (element instanceof Object[]) {
                        Object[] obj = (Object[]) element;
                        tgaObject tga = new tgaObject();
                        // Use String.valueOf to avoid ClassCastException when DB returns non-String types (e.g., Character)
                        tga.setCustid(obj.length > 0 && obj[0] != null ? String.valueOf(obj[0]) : "");
                        tga.setFilter_flag(obj.length > 1 && obj[1] != null ? String.valueOf(obj[1]) : "");
                        tga.setServer(obj.length > 2 && obj[2] != null ? String.valueOf(obj[2]) : "");
                        tga.setDipEngine(obj.length > 3 && obj[3] != null ? String.valueOf(obj[3]) : "");
                        list.add(tga);
                    }
                }
            }
        } catch (Exception sqle) {
            logger.error("Error loading TgaCache: ", sqle);
        }

    }

    public static synchronized TgaCache getSharedCache() {
        if (sharedObj == null) {
            try {
                sharedObj = new TgaCache();
            } catch (SQLException sqlE) {
                logger.error(sqlE);
            } catch (NamingException nE) {
                logger.error(nE);
            }

            return sharedObj;
        } else {
            return sharedObj;
        }
    }

    public static synchronized TgaCache updateSharedCache() {
        try {
            sharedObj = new TgaCache();
        } catch (SQLException sqlE) {
            logger.error(sqlE);
        } catch (NamingException nE) {
            logger.error(nE);
        }

        return sharedObj;
    }

    public synchronized ArrayList<tgaObject> getTgaList() {
        return list;
    }

    public synchronized void setTgaList(ArrayList<tgaObject> list) {
        TgaCache.list = list;
        logger.info(">> TGACache is updated : " + TgaCache.list);
    }
}
