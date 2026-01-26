//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.infobip.webservice.InfobipDipInterface;
import com.isentric.bulkgateway.infobip.webservice.InfobipDipInterfaceService;
import com.isentric.bulkgateway.infobip.webservice.InfobipDipInterfaceServiceLocator;
import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.tga.webservice.QSResponse;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class Infobip161Util {
    private static final Logger logger = LoggerManager.createLoggerPattern(Infobip161Util.class);
    private static Infobip161Util infobidInstance;
    private InfobipDipInterfaceService service = null;
    private InfobipDipInterface port = null;

    public Infobip161Util(String serverIP) throws ServiceException, MalformedURLException {
        URL portAddress = new URL("http://192.168.26.161:8002/InfobipDipping/services/infobipws");
        this.service = new InfobipDipInterfaceServiceLocator();
        this.port = this.service.getInfobipws(portAddress);
    }

    public static Infobip161Util getInstance(String serverIP) throws MalformedURLException {
        try {
            synchronized(Infobip161Util.class) {
                if (infobidInstance == null) {
                    infobidInstance = new Infobip161Util(serverIP);
                }
            }
        } catch (ServiceException sExp) {
            logger.fatal(sExp);
        }

        return infobidInstance;
    }

    public String queryTelcoFromInfobid(String inMSISDN) {
        QSResponse infobidResp = null;
        String retTelco = null;

        try {
            infobidResp = this.port.InfobipDipping(inMSISDN);
            if (infobidResp != null && "200".equals(infobidResp.getRespCode()) && infobidResp.getTelco() != null) {
                retTelco = infobidResp.getTelco();
            } else {
                logger.info("[ MNP Query Fail ]");
                if (infobidResp != null) {
                    logger.info("--------------------");
                    logger.info(infobidResp.getMsisdn());
                    logger.info(infobidResp.getRegionCode());
                    logger.info(infobidResp.getRespCode());
                    logger.info(infobidResp.getTxnId());
                    logger.info(infobidResp.getTelco());
                    logger.info("--------------------");
                }
            }
        } catch (RemoteException rExp) {
            rExp.printStackTrace();
            logger.fatal(rExp);
        }

        return retTelco;
    }

    public String queryTelcoFromInfobidSkipFilter(String inMSISDN) {
        QSResponse infobidResp = null;
        String retTelco = null;

        try {
            infobidResp = this.port.InfobipDippingSkipFilter(inMSISDN);
            if (infobidResp != null && "200".equals(infobidResp.getRespCode()) && infobidResp.getTelco() != null) {
                retTelco = infobidResp.getTelco();
            } else {
                logger.info("[ MNP Query Fail ]");
                if (infobidResp != null) {
                    logger.info("--------------------");
                    logger.info(infobidResp.getMsisdn());
                    logger.info(infobidResp.getRegionCode());
                    logger.info(infobidResp.getRespCode());
                    logger.info(infobidResp.getTxnId());
                    logger.info(infobidResp.getTelco());
                    logger.info("--------------------");
                }
            }
        } catch (RemoteException rExp) {
            rExp.printStackTrace();
            logger.fatal(rExp);
        }

        return retTelco;
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.print(getInstance("161").queryTelcoFromInfobidSkipFilter("60126669590"));
    }
}
