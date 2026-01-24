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
    Dao dao = new Dao();
    private static TgaCache sharedObj = null;
    private static ArrayList<tgaObject> list = null;

    public TgaCache() throws NamingException, SQLException {
        String sql = "select custid, filter_flag, server, dip_engine from bulk_config.specific_tga ";
        logger.info("TgaCache init()");

        try {
            List<Object> result = this.dao.query(sql);
            if (!result.isEmpty()) {
                list = new ArrayList<>();

                for (Object element : result) {
                    if (element instanceof Object[]) {
                        Object[] obj = (Object[]) element;
                        tgaObject tga = new tgaObject();
                        tga.setCustid((String) obj[0]);
                        tga.setFilter_flag((String) obj[1]);
                        tga.setServer((String) obj[2]);
                        tga.setDipEngine((String) obj[3]);
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
