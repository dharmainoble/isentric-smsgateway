//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.utility;

import com.isentric.bg.manager.LoggerManager;
import com.isentric.bg.tga.webservice.QSQueryService;
import com.isentric.bg.tga.webservice.QSQueryServiceLocator;
import com.isentric.bg.tga.webservice.QSQuery_PortType;
import com.isentric.bg.tga.webservice.QSResponse;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.URL;
import java.rmi.RemoteException;

public class TGAUtilSkipFilter {
    private static final Logger logger = LoggerManager.createLoggerPattern(TGAUtilSkipFilter.class);
    private static TGAUtilSkipFilter tgaInstance;
    private QSQueryService service = null;
    private QSQuery_PortType port = null;

    public TGAUtilSkipFilter() throws ServiceException {
        try {
            URL portAddress = new URL("http://192.168.26.100:8280/TGA-Internal/services/QSQuery?wsdl");
            this.service = new QSQueryServiceLocator();
            this.port = this.service.getQSQuery(portAddress);
        } catch (Exception var2) {
        }

    }

    public static TGAUtilSkipFilter getInstance() {
        try {
            synchronized(TGAUtilSkipFilter.class) {
                if (tgaInstance == null) {
                    tgaInstance = new TGAUtilSkipFilter();
                }
            }
        } catch (ServiceException sExp) {
            logger.fatal(sExp);
        }

        return tgaInstance;
    }

    public String queryTelcoFromTGA(String inMSISDN) {
        QSResponse qsResp = null;
        String retTelco = null;

        try {
            qsResp = this.port.queryTGA(inMSISDN);
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

    public static void main(String[] args) {
        System.out.print(getInstance().queryTelcoFromTGA("60173277115"));
    }
}
