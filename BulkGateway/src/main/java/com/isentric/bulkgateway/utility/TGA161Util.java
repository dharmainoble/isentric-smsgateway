//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.tga.webservice.QSQueryService;
import com.isentric.bulkgateway.tga.webservice.QSQueryServiceLocator;
import com.isentric.bulkgateway.tga.webservice.QSQuery_PortType;
import com.isentric.bulkgateway.tga.webservice.QSResponse;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class TGA161Util {
    private static final Logger logger = LoggerManager.createLoggerPattern(TGA161Util.class);
    private static TGA161Util tgaInstance;
    private QSQueryService service = null;
    private QSQuery_PortType port = null;

   /* public TGA161Util(String serverIP) throws ServiceException, MalformedURLException {
        URL portAddress = new URL("http://192.168.26.161:8002/TGA/services/QSQuery");
        this.service = new QSQueryServiceLocator();
        this.port = this.service.getQSQuery(portAddress);
    }*/

   /* public static TGA161Util getInstance(String serverIP) throws MalformedURLException {
        try {
            synchronized(TGA161Util.class) {
                if (tgaInstance == null) {
                    tgaInstance = new TGA161Util(serverIP);
                }
            }
        } catch (ServiceException sExp) {
            logger.fatal(sExp);
        }

        return tgaInstance;
    }*/

    public String queryTelcoFromTGA(String inMSISDN) {
        QSResponse qsResp = null;
        String retTelco = null;

        try {
            qsResp = this.port.queryTGA(inMSISDN);
            System.out.println("queryTelcoFromTGA - "+qsResp.getTelco());
            System.out.println("qsResp.getRespCode() - "+qsResp.getRespCode());
            if (qsResp != null && "0000".equals(qsResp.getRespCode()) && qsResp.getTelco() != null) {
                retTelco = qsResp.getTelco();
            } else {
                logger.info("[ MNP Query Fail ]");
                if (qsResp != null) {
                    logger.info("--------------------");
                    logger.info(qsResp.getMsisdn());
                    logger.info(qsResp.getRegionCode());
                    logger.info(qsResp.getRespCode());
                    logger.info(qsResp.getTxnId());
                    logger.info(qsResp.getTelco());
                    logger.info("--------------------");
                }
            }
        } catch (RemoteException rExp) {
            rExp.printStackTrace();
            logger.fatal(rExp);
        }

        return retTelco;
    }

    public String queryTelcoFromTGASkipFilter(String inMSISDN) {
        QSResponse qsResp = null;
        String retTelco = null;

        try {
            qsResp = this.port.queryTGASkipFilter(inMSISDN);
            if (qsResp != null && "0000".equals(qsResp.getRespCode()) && qsResp.getTelco() != null) {
                retTelco = qsResp.getTelco();
            } else {
                logger.info("[ MNP Query Fail ]");
                if (qsResp != null) {
                    logger.info("--------------------");
                    logger.info(qsResp.getMsisdn());
                    logger.info(qsResp.getRegionCode());
                    logger.info(qsResp.getRespCode());
                    logger.info(qsResp.getTxnId());
                    logger.info(qsResp.getTelco());
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
        //System.out.print(getInstance("118").queryTelcoFromTGA("60173277115"));
    }
}
