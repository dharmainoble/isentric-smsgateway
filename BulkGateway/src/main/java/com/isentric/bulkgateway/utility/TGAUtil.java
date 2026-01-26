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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TGAUtil {
    private static final Logger logger = LoggerManager.createLoggerPattern(TGAUtil.class);
    private static TGAUtil tgaInstance;
    private QSQueryService service = null;
    private QSQuery_PortType port = null;

    public TGAUtil(String serverIP) throws ServiceException, MalformedURLException {
        URL portAddress = new URL("http://192.168.26.100:8001/TGA/services/QSQuery");
        this.service = new QSQueryServiceLocator();
        this.port = this.service.getQSQuery(portAddress);
    }

    public static TGAUtil getInstance(String serverIP) throws MalformedURLException {
        try {
            synchronized(TGAUtil.class) {
                if (tgaInstance == null) {
                    tgaInstance = new TGAUtil(serverIP);
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
            logger.error("Error in queryTelcoFromTGA", rExp);
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
            logger.error("Error in queryTelcoFromTGASkipFilter", rExp);
            logger.fatal(rExp);
        }

        return retTelco;
    }

    public String queryTelcoFromTGAWeek(String inMSISDN) {
        QSResponse qsResp = null;
        String retTelco = null;

        try {
            // Note: Using queryTGA since queryTGAWeek is not available in the web service interface
            // Week-based filtering should be handled by the server or post-processing
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
            logger.error("Error in queryTelcoFromTGAWeek", rExp);
            logger.fatal(rExp);
        }

        return retTelco;
    }

    public static String manualQueryTelco(String msisdn) {
        String telco = "YTL";
        Dao dao = new Dao();
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -60);
        Date previous60day = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String previousDate = sdf.format(previous60day);
        String queryTelco = "select resp_telco,tx_id,msisdn,resp_code,resp_region_code from extmt.tbl_qs_transaction WHERE msisdn = '" + msisdn + "' and query_datetime>='" + previousDate + "' order by row_id desc limit 1;";
        logger.info("queryTelco>>" + queryTelco);

        try {
            ArrayList<Object> rows = (ArrayList<Object>)dao.query(queryTelco);

            for(Object element : rows) {
                if (element instanceof Object[]) {
                    Object[] cols = (Object[]) element;
                    telco = String.valueOf(cols[0]);
                    logger.info("telco=" + telco);
                }
            }
        } catch (Exception ex) {
            logger.info("TGAUtil:CheckDBFailed!e=" + ex.getMessage());
        }

        return telco;
    }

    public static String manualQueryTelcoWeek(String msisdn) {
        String telco = "YTL";
        Dao dao = new Dao();
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date previous07day = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String previousDate = sdf.format(previous07day);
        String queryTelco = "select resp_telco,tx_id,msisdn,resp_code,resp_region_code from extmt.tbl_qs_transaction WHERE msisdn = '" + msisdn + "' and query_datetime>='" + previousDate + "' order by row_id desc limit 1;";
        logger.info("queryTelco>>" + queryTelco);

        try {
            ArrayList<Object> rows = (ArrayList<Object>)dao.query(queryTelco);

            for(Object element : rows) {
                if (element instanceof Object[]) {
                    Object[] cols = (Object[]) element;
                    telco = String.valueOf(cols[0]);
                    logger.info("telco=" + telco);
                }
            }
        } catch (Exception ex) {
            logger.info("TGAUtil:CheckDBFailed!e=" + ex.getMessage());
        }

        return telco;
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.print(getInstance("118").queryTelcoFromTGA("60122599450"));
    }
}
