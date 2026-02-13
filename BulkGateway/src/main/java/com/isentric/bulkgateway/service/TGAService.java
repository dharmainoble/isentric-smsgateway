package com.isentric.bulkgateway.service;


import com.isentric.bulkgateway.constant.Constant;
import com.isentric.bulkgateway.repository.BulkSkipAutoResendRepository;
import com.isentric.bulkgateway.repository.TagRepository;
import com.isentric.bulkgateway.tga.webservice.QSResponse;
import com.isentric.bulkgateway.tga.webservice.QSSoapService;
import com.isentric.bulkgateway.tga.webservice.QSSoapServiceLocator;
import com.isentric.bulkgateway.tga.webservice.QSSoapServicePortType;
import com.isentric.bulkgateway.util.XMLUtil;
import org.apache.axis.encoding.Base64;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class TGAService {


    public QSResponse queryTGA(String in0) throws RemoteException {
        QSResponse qsresponse = null;

        try {
            if (in0 != null && in0.length() > 0) {
                try {
                    qsresponse = this.checkTelcoPeriod(in0);
                } catch (Exception var12) {
                    qsresponse = new QSResponse();
                    qsresponse.setTelco("Rediping");
                    System.out.println("Invoke Get DB Instance Failed!");
                }

                if ("Rediping".equalsIgnoreCase(qsresponse.getTelco())) {
                    System.out.println("Redipping");
                    Base64 base64 = new Base64();
                    XMLUtil xml = new XMLUtil();
                    TagRepository repo = new TagRepository();
                    URL portAddress = new URL(Constant.QS_URL);
                    QSSoapService service = new QSSoapServiceLocator();
                    QSSoapServicePortType port = service.getQSSoapServiceSOAP11port_http(portAddress);
                    String query = xml.generateQSXML(in0);
                    String result = port.queryRN(Constant.USERID, Base64.encode(Constant.PASSWORD.getBytes()).getBytes(), query);
                    qsresponse = xml.getQSResponseDetailXML(result);
                    String sql = "insert into extmt.tbl_qs_transaction(tx_id,msisdn,resp_code,resp_telco,resp_region_code,query_datetime) values('" + qsresponse.getTxnId() + "','" + qsresponse.getMsisdn() + "','" + qsresponse.getRespCode() + "','" + qsresponse.getTelco() + "','" + qsresponse.getRegionCode() + "',now())";
                    repo.update(sql, "fermat");
                } else {
                    System.out.println("Get Telco From DB as the dipping Period < 2 months");
                    System.out.println("DB data = " + qsresponse.toString());
                }
            } else {
                System.out.println("Empty msisdn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qsresponse;
    }

    public QSResponse queryTGASkipFilter(String in0) throws RemoteException {
        QSResponse qsresponse = null;

        try {
            if (in0 != null && in0.length() > 0) {
                new Base64();
                XMLUtil xml = new XMLUtil();
                TagRepository repo = new TagRepository();
                URL portAddress = new URL(Constant.QS_URL);
                System.out.println("----------portAddress-----------------" + portAddress);
                QSSoapService service = new QSSoapServiceLocator();
                QSSoapServicePortType port = service.getQSSoapServiceSOAP11port_http(portAddress);
                String query = xml.generateQSXML(in0);
                String result = port.queryRN(Constant.USERID, Base64.encode(Constant.PASSWORD.getBytes()).getBytes(), query);
                qsresponse = xml.getQSResponseDetailXML(result);
                System.out.println("------------qsresponse-----------------------" + qsresponse);
                String sql = "insert into extmt.tbl_qs_transaction(tx_id,msisdn,resp_code,resp_telco,resp_region_code,query_datetime) values('" + qsresponse.getTxnId() + "','" + qsresponse.getMsisdn() + "','" + qsresponse.getRespCode() + "','" + qsresponse.getTelco() + "','" + qsresponse.getRegionCode() + "',now())";
                repo.update(sql, "fermat");
            } else {
                System.out.println("Empty msisdn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qsresponse;
    }

    public QSResponse checkTelcoPeriod(String msisdn) {
        QSResponse tempQSResponse = new QSResponse();
        tempQSResponse.setTelco("Rediping");

        if (msisdn == null || msisdn.trim().isEmpty()) {
            System.out.println("Invalid msisdn provided");
            return tempQSResponse;
        }

        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.add(5, -60);
        Date previous60day = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String previousDate = sdf.format(previous60day);
        String queryTelco = "select resp_telco,tx_id,msisdn,resp_code,resp_region_code from extmt.tbl_qs_transaction WHERE msisdn = '" + msisdn + "' and query_datetime>='" + previousDate + "' order by row_id desc limit 1;";

        try {
            TagRepository repo = new TagRepository();

            ArrayList<?> rs = (ArrayList<?>) repo.query(queryTelco, "fermat2");

            // Null check before accessing rs to prevent NullPointerException
            if (rs != null && !rs.isEmpty()) {
                Object[] cols = (Object[]) rs.get(0);
                if (cols != null && cols.length >= 5) {
                    tempQSResponse.setTelco(String.valueOf(cols[0]));
                    tempQSResponse.setTxnId(String.valueOf(cols[1]));
                    tempQSResponse.setMsisdn(String.valueOf(cols[2]));
                    tempQSResponse.setRespCode(String.valueOf(cols[3]));
                    tempQSResponse.setRegionCode(String.valueOf(cols[4]));
                }
            }

            if (tempQSResponse.getTelco() == null || tempQSResponse.getTelco().equalsIgnoreCase("Rediping")) {
                tempQSResponse.setTelco("Rediping");
            }
        } catch (Exception var15) {
            tempQSResponse.setTelco("Rediping");
            System.out.println("CheckDBFailed! Error: " + var15.getMessage());
            var15.printStackTrace();
        }

        return tempQSResponse;
    }

}
