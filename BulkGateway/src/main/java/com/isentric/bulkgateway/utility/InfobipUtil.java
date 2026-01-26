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

public class InfobipUtil {
    private static final Logger logger = LoggerManager.createLoggerPattern(InfobipUtil.class);
    private static InfobipUtil infobidInstance;
    private InfobipDipInterfaceService service = null;
    private InfobipDipInterface port = null;

    public InfobipUtil(String serverIP) throws ServiceException, MalformedURLException {
        URL portAddress = new URL("http://203.223.130.118:8001/InfobipDipping/services/infobipws");
        this.service = new InfobipDipInterfaceServiceLocator();
        this.port = this.service.getInfobipws(portAddress);
    }

    public static InfobipUtil getInstance(String serverIP) throws MalformedURLException {
        try {
            synchronized(InfobipUtil.class) {
                if (infobidInstance == null) {
                    infobidInstance = new InfobipUtil(serverIP);
                }
            }
        } catch (ServiceException sExp) {
            logger.fatal(sExp);
        }

        return infobidInstance;
    }

    public String queryTelcoFromInfobid(String inMSISDN) {
        QSResponse infobipResp = null;
        String retTelco = null;

        try {
            logger.info("<<<<<<<<<<<<<<<< Infobip Start Dipping >>>>>>>>>>>>>>>>");
            infobipResp = this.port.InfobipDipping(inMSISDN);
            if (infobipResp != null && "200".equals(infobipResp.getRespCode()) && infobipResp.getTelco() != null) {
                retTelco = infobipResp.getTelco();
            } else {
                logger.info("[ MNP Query Fail ]");
                if (infobipResp != null) {
                    logger.info("--------------------");
                    logger.info(infobipResp.getMsisdn());
                    logger.info(infobipResp.getRegionCode());
                    logger.info(infobipResp.getRespCode());
                    logger.info(infobipResp.getTxnId());
                    logger.info(infobipResp.getTelco());
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
        QSResponse infobipResp = null;
        String retTelco = null;

        try {
            infobipResp = this.port.InfobipDippingSkipFilter(inMSISDN);
            if (infobipResp != null && "200".equals(infobipResp.getRespCode()) && infobipResp.getTelco() != null) {
                retTelco = infobipResp.getTelco();
            } else {
                logger.info("[ MNP Query Fail ]");
                if (infobipResp != null) {
                    logger.info("--------------------");
                    logger.info(infobipResp.getMsisdn());
                    logger.info(infobipResp.getRegionCode());
                    logger.info(infobipResp.getRespCode());
                    logger.info(infobipResp.getTxnId());
                    logger.info(infobipResp.getTelco());
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
        System.out.print(getInstance("118").queryTelcoFromInfobidSkipFilter("60126669590"));
    }
}
