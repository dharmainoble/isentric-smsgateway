package com.isentric.bulkgateway.mdb;

import com.isentric.bulkgateway.bg.model.SMSMessageSmpp;
import com.isentric.bulkgateway.dto.tgaObject;
import com.isentric.bulkgateway.exception.MessageException;
import com.isentric.bulkgateway.exception.SMSException;
import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.manager.MessagesManager;
import com.isentric.bulkgateway.manager.PrefixManager;
import com.isentric.bulkgateway.manager.SpecificRouteManager;
import com.isentric.bulkgateway.service.SmppMessageServiceBinder;
import com.isentric.bulkgateway.service.TgaSoapService;
import com.isentric.bulkgateway.utility.*;
import org.apache.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import jakarta.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class SMSServerSMPP implements ApplicationContextAware {

    // Static context holder for accessing Spring beans
    private static ApplicationContext applicationContext;

    @Autowired
    private TgaSoapService tgaSoapService;

    // Constructor to set the application context
    public SMSServerSMPP(ApplicationContext context) {
        SMSServerSMPP.applicationContext = context;
        logger.info("SMSServerSMPP initialized with ApplicationContext");
    }

    // Default constructor for Spring
    public SMSServerSMPP() {
    }

    // Implement ApplicationContextAware interface
    @Override
    public void setApplicationContext(ApplicationContext context) {
        SMSServerSMPP.applicationContext = context;
        logger.info("ApplicationContext set via ApplicationContextAware");
    }

    // PostConstruct method to verify initialization
    @PostConstruct
    public void init() {
        logger.info("SMSServerSMPP initialization check: ApplicationContext=" +
                   (applicationContext != null ? "AVAILABLE" : "NULL") +
                   ", TgaSoapService=" + (tgaSoapService != null ? "AVAILABLE" : "NULL"));

        // Try to get TgaSoapService early if not autowired
        if (tgaSoapService == null && applicationContext != null) {
            try {
                tgaSoapService = applicationContext.getBean(TgaSoapService.class);
                logger.info("TgaSoapService successfully retrieved during PostConstruct");
            } catch (Exception e) {
                logger.warn("Could not retrieve TgaSoapService during PostConstruct: " + e.getMessage());
            }
        }
    }

    // Method to get TgaSoapService safely
    private TgaSoapService getTgaSoapService() {
        // Level 1: Check autowired field
        if (this.tgaSoapService != null) {
            logger.debug("TgaSoapService retrieved from autowired field");
            return this.tgaSoapService;
        }

        // Level 2: Try to get from ApplicationContext
        if (applicationContext != null) {
            try {
                TgaSoapService service = applicationContext.getBean(TgaSoapService.class);
                logger.debug("TgaSoapService retrieved from ApplicationContext");
                return service;
            } catch (Exception e) {
                logger.error("Failed to get TgaSoapService from ApplicationContext: " + e.getMessage(), e);
            }
        } else {
            logger.error("ApplicationContext is null - Spring context not properly initialized");
        }

        // Level 3: Provide detailed error message
        String errorMsg = "TgaSoapService is not available. " +
                         "ApplicationContext: " + (applicationContext != null ? "Available" : "NULL") +
                         ", Autowired Bean: " + (this.tgaSoapService != null ? "Available" : "NULL") +
                         ". Check if TgaSoapService bean is configured and application context is initialized.";
        logger.error(errorMsg);
        throw new RuntimeException(errorMsg);
    }

        private static final long serialVersionUID = -5597886878125332146L;
        private static final Logger logger = LoggerManager.createLoggerPattern(SMSServerSMPP.class);
        private static final int restartAttempt = 3;
        private static final long restartPaused = 5000L;
        private Exception exception = null;


        public void onMessage(SMSMessageSmpp smsMessageSmpp) {
        SmppMessageServiceBinder smppMessageServiceBinder = null;
        String tempTelco = null;
        String smppName = null;
        String guid = null;

        try {
                guid = smsMessageSmpp.getGuid();

               /* try {
                    if (smsMessageSmpp.getCallbackURL().contains("<citibankqueue>")) {
                        StartStopQueueManager cache5 = StartStopQueueManager.getInstance();
                        String queue_name = "queue/DuplicateQciti";
                        String queueSeq = smsMessageSmpp.getCallbackURL().split("<citibankqueue>")[1].toString();
                        if (cache5.getMessagesObj(queueSeq).getEnable_flag() != null && cache5.getMessagesObj(queueSeq).getEnable_flag().equalsIgnoreCase("1")) {
                            JmsUtil jmsUtil = new JmsUtil();
                            jmsUtil.postQueue(queue_name + queueSeq, smsMessageSmpp);
                            return;
                        }

                        smsMessageSmpp.setCallbackURL(smsMessageSmpp.getCallbackURL().split("<citibankqueue>")[0].toString());
                    } else if (smsMessageSmpp.getCallbackURL().contains("<rhbbankqueue>")) {
                        StartStopQueueManager cache6 = StartStopQueueManager.getInstance();
                        String queue_name = "queue/DuplicateQrhb";
                        String queueSeq = smsMessageSmpp.getCallbackURL().split("<rhbbankqueue>")[1].toString();
                        if (cache6.getMessagesObj(queueSeq).getEnable_flag() != null && cache6.getMessagesObj(queueSeq).getEnable_flag().equalsIgnoreCase("1")) {
                            JmsUtil jmsUtil = new JmsUtil();
                            jmsUtil.postQueue(queue_name + queueSeq, smsMessageSmpp);
                            return;
                        }

                        smsMessageSmpp.setCallbackURL(smsMessageSmpp.getCallbackURL().split("<rhbbankqueue>")[0].toString());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.getMessage());
                }*/

                smppMessageServiceBinder = Ejb3Util.getSmppMessageServiceBinder();
                smppName = smsMessageSmpp.getSmppName();
            System.out.println("smppName1 -"+smppName);
                boolean flag = true;
                PrefixManager cache = PrefixManager.getInstance();
                SpecificRouteManager cache2 = SpecificRouteManager.getInstance();
                if (!smsMessageSmpp.getTelco().equals("other")) {
                    if (cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute() == null) {
                        flag = false;
                    } else if (cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute().equals("")) {
                        flag = false;
                    } else {
                        System.out.println("smsMessageSmpp.getTelco() -"+smsMessageSmpp.getTelco());
                        smsMessageSmpp.setCredit(cache.getPrefixObj(smsMessageSmpp.getTelco()).getCredit());
                        smsMessageSmpp.setSmppName(cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute());
                        smppName = smsMessageSmpp.getSmppName();
                        System.out.println("smppName2 -"+smppName);
                        flag = true;
                        System.out.println("credit -"+smsMessageSmpp.getCredit());
                        try {
                            if (smsMessageSmpp.getCredit() == null) {
                                String strCredit = "select route, credit from bulk_config.credit_control where country='" + smsMessageSmpp.getTelco() + "'";
                                try {
                                    java.util.List<Object[]> rows = com.isentric.bulkgateway.utility.EntityManagerFactoryProvider.executeNativeQueryAsArray("Bulk Config", strCredit);
                                    if (rows != null && !rows.isEmpty()) {
                                        for (Object[] resRow : rows) {
                                            if (resRow != null && resRow.length >= 2) {
                                                if (resRow[0] != null) smsMessageSmpp.setSmppConfig(resRow[0].toString());
                                                if (resRow[1] != null) smsMessageSmpp.setCredit(resRow[1].toString());
                                            }
                                        }
                                    }
                                } catch (Exception queryEx) {
                                    logger.error("Error querying credit_control via EMF: " + queryEx.getMessage(), queryEx);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error in checking credit >>>" + e.getMessage(), e);
                        }
                    }
                } else {
                    flag = false;
                }

                if (cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()) != null && !cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute().equals("")) {
                    smsMessageSmpp.setSmppName(cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute());
                    smppName = smsMessageSmpp.getSmppName();
                    System.out.println("smppName3 -"+smppName);
                }

                if (cache.getPrefixObj(smsMessageSmpp.getTelco()) != null || cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()) != null && cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getTgaToggle() != 1) {
                    String serverIP = null;
                    String dipEngine = null;

                    try {
                        String tempKeyword = smsMessageSmpp.getKeyword();
                        if (tempKeyword.startsWith("extmt_")) {
                            tempKeyword = tempKeyword.substring(6);
                        }

                        ArrayList testCache = TgaCache.getSharedCache().getTgaList();
                        tgaObject tgaObject = null;
                        boolean inList = false;

                        for(int i = 0; i < testCache.size(); ++i) {
                            tgaObject = (tgaObject)testCache.get(i);
                            if (tgaObject.getCustid().equalsIgnoreCase(tempKeyword)) {
                                serverIP = tgaObject.getServer();
                                dipEngine = tgaObject.getDipEngine();
                            }

                            if (tgaObject.getCustid().equalsIgnoreCase(tempKeyword) && tgaObject.getFilter_flag().equalsIgnoreCase("0")) {
                                inList = true;
                                break;
                            }
                        }
                        System.out.println(dipEngine);
                        System.out.println(serverIP);
                        System.out.println("inList - "+ inList);

                        if (inList) {
                            if (dipEngine != null && dipEngine.equals("Infobip")) {
                                if (serverIP != null && serverIP.equals("161")) {
                                    tempTelco = Infobip161Util.getInstance(serverIP).queryTelcoFromInfobid(smsMessageSmpp.getRecipient());
                                } else {
                                    tempTelco = InfobipUtil.getInstance(serverIP).queryTelcoFromInfobid(smsMessageSmpp.getRecipient());
                                }
                            } else if (serverIP != null && serverIP.equals("161")) {
                                TGA161Util utl=new TGA161Util(smsMessageSmpp.getRecipient());
                                //tempTelco = utl.queryTelcoFromTGA(smsMessageSmpp.getRecipient());
                                tempTelco = TGA161Util.getInstance(serverIP).queryTelcoFromTGA(smsMessageSmpp.getRecipient());
                            } else {
                                System.out.println("New Tele IS :");
                                String response = getTgaSoapService().callTga(smsMessageSmpp.getRecipient());
                                tempTelco =getTelco(response);
                                System.out.println(tempTelco);
                               // tempTelco = TGAUtil.getInstance(serverIP).queryTelcoFromTGA(smsMessageSmpp.getRecipient());
                            }
                        } else if (dipEngine != null && dipEngine.equals("Infobip")) {
                            if (serverIP != null && serverIP.equals("161")) {
                                tempTelco = Infobip161Util.getInstance(serverIP).queryTelcoFromInfobidSkipFilter(smsMessageSmpp.getRecipient());
                            } else {
                                tempTelco = InfobipUtil.getInstance(serverIP).queryTelcoFromInfobidSkipFilter(smsMessageSmpp.getRecipient());
                            }
                        } else if (serverIP != null && serverIP.equals("161")) {
                            TGA161Util utl=new TGA161Util(smsMessageSmpp.getRecipient());
                           // tempTelco = utl.queryTelcoFromTGA(smsMessageSmpp.getRecipient());
                            tempTelco = TGA161Util.getInstance(serverIP).queryTelcoFromTGA(smsMessageSmpp.getRecipient());
                        } else {
                            System.out.println("New Tele IS :");
                            String response = getTgaSoapService().callTga(smsMessageSmpp.getRecipient());
                            tempTelco =getTelco(response);
                            System.out.println(tempTelco);
                            //tempTelco = TGAUtil.getInstance(serverIP).queryTelcoFromTGASkipFilter(smsMessageSmpp.getRecipient());
                        }
                    } catch (Exception var43) {
                        String tempKeyword = smsMessageSmpp.getKeyword();
                        if (tempKeyword.startsWith("extmt_")) {
                            tempKeyword = tempKeyword.substring(6);
                        }

                        if (dipEngine != null && dipEngine.equals("Infobip")) {
                            logger.debug(tempKeyword + ": Infobip-Skip-Filter-Exception' ; Guid = " + smsMessageSmpp.getGuid() + ",tempTelco=" + tempTelco + ",recipient=" + smsMessageSmpp.getRecipient());
                        } else {
                            tempTelco = TGAUtil.manualQueryTelco(smsMessageSmpp.getRecipient());
                        }
                        var43.printStackTrace();
                        logger.debug(tempKeyword + ": TGA-Skip-Filter-Exception' ; Guid = " + smsMessageSmpp.getGuid() + ",tempTelco=" + tempTelco);
                    }

                    System.out.println("tempTelco -"+tempTelco);
                    if (tempTelco != null && tempTelco.trim().length() != 0 && !smsMessageSmpp.getTelco().trim().equals(tempTelco.trim())) {
                        smsMessageSmpp.setTelco(tempTelco);
                        System.out.println(tempTelco);
                        System.out.println(cache.getPrefixObj(smsMessageSmpp.getTelco()));
                        smsMessageSmpp.setSmppName(cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute());
                        smppName = smsMessageSmpp.getSmppName();
                        System.out.println("smppName4 -"+smppName);
                    }

                    if (cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()) != null && !cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute().equals("") && cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getTgaToggle() == 0) {
                        smsMessageSmpp.setSmppName(cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute());
                        smppName = smsMessageSmpp.getSmppName();
                        System.out.println("smppName5 -"+smppName);
                    }
                }

                if (smppName.equalsIgnoreCase("SMPP_TUNETALK_118")) {
                    smppName = "SMPP_TUNETALK_115";
                }

                String keyword;
                if (smsMessageSmpp.getKeyword().startsWith("extmt_")) {
                    keyword = smsMessageSmpp.getKeyword().substring(6);
                } else {
                    keyword = smsMessageSmpp.getKeyword();
                }

                MessagesManager messagesCache = MessagesManager.getInstance();
                if (messagesCache.getMessagesObj(keyword) != null) {
                    smsMessageSmpp.setMessage(messagesCache.getMessagesObj(keyword).getMessages());
                    logger.info("Update Cust ID >>> " + smsMessageSmpp.getKeyword() + " with messages >>> " + smsMessageSmpp.getMessage());
                }

                if (!flag) {
                    logger.info("messege not send to " + smsMessageSmpp.getRecipient());
                } else {
                    for(int attemptCount = 0; attemptCount < 3 && !smppMessageServiceBinder.isAlive(smppName) && !smppMessageServiceBinder.isConnected(smppName); ++attemptCount) {
                        logger.info("[" + attemptCount++ + "] SMPP connection is not alive or connected, restarting ...");
                        smppMessageServiceBinder.destroySmpp(smppName);
                        Thread.sleep(5000L);

                        try {
                                // Query extmt.route_config via EntityManagerFactory (try startup_115 = 0 first, then fallback)
                                java.util.List<Object[]> routeInfo = null;
                                String sql1 = "select routeName, configType, configFile, apiKey from extmt.route_config where routeName='" + smppName + "' and startup_115 = 0";
                                String sql2 = "select routeName, configType, configFile, apiKey from extmt.route_config where routeName='" + smppName + "'";
                                try {
                                    // query against the extmt schema using the 'extmt' persistence unit configured in ExtmtDBConfig
                                    routeInfo = com.isentric.bulkgateway.utility.EntityManagerFactoryProvider.executeNativeQueryAsArray("extmt", sql1);
                                } catch (Exception q1) {
                                    logger.error("Error running route_config query (sql1): " + q1.getMessage(), q1);
                                }
                                if (routeInfo == null || routeInfo.isEmpty()) {
                                    try {
                                        routeInfo = com.isentric.bulkgateway.utility.EntityManagerFactoryProvider.executeNativeQueryAsArray("extmt", sql2);
                                    } catch (Exception q2) {
                                        logger.error("Error running route_config fallback query (sql2): " + q2.getMessage(), q2);
                                    }
                                }
                                Thread.sleep(5000L);

                                if (routeInfo != null) {
                                    System.out.println("smppName2 -"+smppName);
                                    for (Object[] cols : routeInfo) {
                                        if (cols == null || cols.length < 4) continue;
                                        String routeName = cols[0] != null ? cols[0].toString() : null;
                                        String configType = cols[1] != null ? cols[1].toString() : null;
                                        String config = cols[2] != null ? cols[2].toString() : null;
                                        String apiKey = cols[3] != null ? cols[3].toString() : null;
                                        if ("smpp".equals(configType)) {
                                            smppMessageServiceBinder.setupSmpp(smppName, config);
                                            logger.info("SMPP " + routeName + " initiation status: " + smppMessageServiceBinder.isInitialized(routeName));
                                            logger.info("SMPP " + routeName + " connection status: " + smppMessageServiceBinder.isConnected(routeName));
                                        } else if ("ucp".equals(configType)) {
                                            smppMessageServiceBinder.setupUcp(smppName, config);
                                            logger.info("UCP " + routeName + " initiation status: " + smppMessageServiceBinder.isInitialized(routeName));
                                            logger.info("UCP " + routeName + " connection status: " + smppMessageServiceBinder.isConnected(routeName));
                                        } else if ("http".equals(configType)) {
                                            smppMessageServiceBinder.setupHttp(routeName, config, apiKey);
                                            logger.info("Http " + routeName + " initiation status: Successful");
                                            logger.info("Http " + routeName + " connection status: Successful");
                                        } else if ("gsm".equals(configType)) {
                                            smppMessageServiceBinder.setupGSM(routeName, config, false);
                                            logger.info("GSM " + routeName + " initiation status: Successful");
                                            logger.info("GSM " + routeName + " connection status: Successful");
                                        } else if ("wsdl".equals(configType)) {
                                            smppMessageServiceBinder.setupWsdl(routeName, config);
                                            logger.info("WSDL " + routeName + " initiation status : Sucessful");
                                            logger.info("WSDL " + routeName + " connection status : Successful");
                                        } else if ("charge".equals(configType)) {
                                            smppMessageServiceBinder.setupChargeService(routeName, config);
                                            logger.info("Charge Service " + routeName + " initiation status : Sucessful");
                                            logger.info("Charge Service " + routeName + " connection status : Successful");
                                        }
                                    }
                                }
                            } catch (Exception sqlE) {
                                sqlE.printStackTrace();
                                logger.fatal(sqlE);
                        }
                    }
                    System.out.println("smppName9 -"+smppName);
                    System.out.println(smppMessageServiceBinder.isAlive(smppName));
                    System.out.println(smppMessageServiceBinder.isConnected(smppName));
                    if (smppMessageServiceBinder.isAlive(smppName) && smppMessageServiceBinder.isConnected(smppName)) {
                        smppMessageServiceBinder.sendSmsMessageSmpp(smppName, smsMessageSmpp.getCredit(), smsMessageSmpp);
                    } else {
                        throw new SMSException("Connection " + smppName + " is [STILL] not alive or connected, resubmiting the message ...");
                    }
                }

        } catch (MessageException msgExp) {
            logger.fatal(msgExp.getMessage());
            this.exception = msgExp;
        } catch (CacheException e) {
            throw new RuntimeException(e);
        } catch (com.objectxp.msg.MessageException e) {
            throw new RuntimeException(e);
        } catch (SMSException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (this.exception != null) {
                SmsUtil.logExceptionSmppServer(this.exception);
            }

        }
    }

        private static String checkCustid(String custid) {
        return custid.startsWith("extmt_") ? custid.replace("extmt_", "") : custid;
    }


    public static String getTelco(String xmlResponse) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(
                new ByteArrayInputStream(xmlResponse.getBytes()));

        NodeList nodeList = doc.getElementsByTagName("telco");

        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }

        return null;
    }


}
