package com.isentric.bulkgateway.service;

import com.isentric.bulkgateway.dto.tgaObject;
import com.isentric.bulkgateway.exception.SMSException;
import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.manager.MessagesManager;
import com.isentric.bulkgateway.manager.PrefixManager;
import com.isentric.bulkgateway.manager.SpecificRouteManager;
import com.isentric.bulkgateway.model.SMSMessageSmpp;
import com.isentric.bulkgateway.utility.*;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class SendQueueSms {

    private static final Logger logger = LoggerManager.createLoggerPattern(SendQueueSms.class);

    public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
        this.mdc = ctx;
    }

    public void onMessage(SMSMessageSmpp smsMessageSmpp) {
        SmppMessageServiceBinder smppMessageServiceBinder = null;
        String tempTelco = null;
        String smppName = null;
        String guid = null;



        try {
                guid = smsMessageSmpp.getGuid();
                /*try {
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
                boolean flag = true;
                PrefixManager cache = PrefixManager.getInstance();
                SpecificRouteManager cache2 = SpecificRouteManager.getInstance();
                if (!smsMessageSmpp.getTelco().equals("other")) {
                    if (cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute() == null) {
                        flag = false;
                    } else if (cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute().equals("")) {
                        flag = false;
                    } else {
                        smsMessageSmpp.setCredit(cache.getPrefixObj(smsMessageSmpp.getTelco()).getCredit());
                        smsMessageSmpp.setSmppName(cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute());
                        smppName = smsMessageSmpp.getSmppName();
                        flag = true;

                        try {
                            if (smsMessageSmpp.getCredit() == null) {
                                Dao queryDOA = new Dao();
                                String strCredit = "select route, credit from bulk_config.credit_control where country='" + smsMessageSmpp.getTelco() + "'";
                                ArrayList rows = (ArrayList)queryDOA.query(strCredit);
                                if (!rows.isEmpty()) {
                                    for(int c = 0; c < rows.size(); ++c) {
                                        Object[] resRow = rows.get(c);
                                        smsMessageSmpp.setSmppConfig(resRow[0].toString());
                                        smsMessageSmpp.setCredit(resRow[1].toString());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error in checking credit >>>" + e.getMessage());
                        }
                    }
                } else {
                    flag = false;
                }

                if (cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()) != null && !cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute().equals("")) {
                    smsMessageSmpp.setSmppName(cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute());
                    smppName = smsMessageSmpp.getSmppName();
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

                        if (inList) {
                            if (dipEngine != null && dipEngine.equals("Infobip")) {
                                if (serverIP != null && serverIP.equals("161")) {
                                    tempTelco = Infobip161Util.getInstance(serverIP).queryTelcoFromInfobid(smsMessageSmpp.getRecipient());
                                } else {
                                    tempTelco = InfobipUtil.getInstance(serverIP).queryTelcoFromInfobid(smsMessageSmpp.getRecipient());
                                }
                            } else if (serverIP != null && serverIP.equals("161")) {
                                tempTelco = TGA161Util.getInstance(serverIP).queryTelcoFromTGA(smsMessageSmpp.getRecipient());
                            } else {
                                tempTelco = TGAUtil.getInstance(serverIP).queryTelcoFromTGA(smsMessageSmpp.getRecipient());
                            }
                        } else if (dipEngine != null && dipEngine.equals("Infobip")) {
                            if (serverIP != null && serverIP.equals("161")) {
                                tempTelco = Infobip161Util.getInstance(serverIP).queryTelcoFromInfobidSkipFilter(smsMessageSmpp.getRecipient());
                            } else {
                                tempTelco = InfobipUtil.getInstance(serverIP).queryTelcoFromInfobidSkipFilter(smsMessageSmpp.getRecipient());
                            }
                        } else if (serverIP != null && serverIP.equals("161")) {
                            tempTelco = TGA161Util.getInstance(serverIP).queryTelcoFromTGASkipFilter(smsMessageSmpp.getRecipient());
                        } else {
                            tempTelco = TGAUtil.getInstance(serverIP).queryTelcoFromTGASkipFilter(smsMessageSmpp.getRecipient());
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

                        logger.debug(tempKeyword + ": TGA-Skip-Filter-Exception' ; Guid = " + smsMessageSmpp.getGuid() + ",tempTelco=" + tempTelco);
                    }

                    if (tempTelco != null && tempTelco.trim().length() != 0 && !smsMessageSmpp.getTelco().trim().equals(tempTelco.trim())) {
                        smsMessageSmpp.setTelco(tempTelco);
                        smsMessageSmpp.setSmppName(cache.getPrefixObj(smsMessageSmpp.getTelco()).getRoute());
                        smppName = smsMessageSmpp.getSmppName();
                    }

                    if (cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()) != null && !cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute().equals("") && cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getTgaToggle() == 0) {
                        smsMessageSmpp.setSmppName(cache2.getPrefixObj(checkCustid(smsMessageSmpp.getKeyword()), smsMessageSmpp.getTelco()).getRoute());
                        smppName = smsMessageSmpp.getSmppName();
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
                    smsMessageSmpp.setMessage(messagesCache.getMessagesObj(keyword).getMessages().getBytes());
                    logger.info("Update Cust ID >>> " + smsMessageSmpp.getKeyword() + " with messages >>> " + smsMessageSmpp.getMessage());
                }

                if (!flag) {
                    logger.info("messege not send to " + smsMessageSmpp.getRecipient());
                } else {
                        try {
                            RouteDAO routeDAO = new RouteDAO();
                            ArrayList routeInfo = routeDAO.findRoute(smppName);
                            Iterator routeIterator = routeInfo.iterator();
                            Thread.sleep(5000L);

                            while(routeIterator.hasNext()) {
                                Object[] cols = routeIterator.next();
                                String routeName = (String)cols[0];
                                String configType = (String)cols[1];
                                String config = (String)cols[2];
                                String apiKey = (String)cols[3];
                                if (configType.equals("smpp")) {
                                    smppMessageServiceBinder.setupSmpp(smppName, config);
                                    logger.info("SMPP " + routeName + " initiation status: " + smppMessageServiceBinder.isInitialized(routeName));
                                    logger.info("SMPP " + routeName + " connection status: " + smppMessageServiceBinder.isConnected(routeName));
                                } else if (configType.equals("ucp")) {
                                    smppMessageServiceBinder.setupUcp(smppName, config);
                                    logger.info("UCP " + routeName + " initiation status: " + smppMessageServiceBinder.isInitialized(routeName));
                                    logger.info("UCP " + routeName + " connection status: " + smppMessageServiceBinder.isConnected(routeName));
                                } else if (configType.equals("http")) {
                                    smppMessageServiceBinder.setupHttp(routeName, config, apiKey);
                                    logger.info("Http " + routeName + " initiation status: Successful");
                                    logger.info("Http " + routeName + " connection status: Successful");
                                } else if (configType.equals("gsm")) {
                                    smppMessageServiceBinder.setupGSM(routeName, config, false);
                                    logger.info("GSM " + routeName + " initiation status: Successful");
                                    logger.info("GSM " + routeName + " connection status: Successful");
                                } else if (configType.equals("wsdl")) {
                                    smppMessageServiceBinder.setupWsdl(routeName, config);
                                    logger.info("WSDL " + routeName + " initiation status : Sucessful");
                                    logger.info("WSDL " + routeName + " connection status : Successful");
                                } else if (configType.equals("charge")) {
                                    smppMessageServiceBinder.setupChargeService(routeName, config);
                                    logger.info("Charge Service " + routeName + " initiation status : Sucessful");
                                    logger.info("Charge Service " + routeName + " connection status : Successful");
                                }
                            }
                        } catch (SQLException sqlE) {
                            sqlE.printStackTrace();
                            logger.fatal(sqlE);
                        }


                    if (smppMessageServiceBinder.isAlive(smppName) && smppMessageServiceBinder.isConnected(smppName)) {
                        smppMessageServiceBinder.sendSmsMessageSmpp(smppName, smsMessageSmpp.getCredit(), smsMessageSmpp);
                    } else {
                        throw new SMSException("Connection " + smppName + " is [STILL] not alive or connected, resubmiting the message ...");
                    }
                }

        } catch (Exception msgExp) {
            logger.fatal(msgExp.getMessage());
            SmsUtil.logExceptionSmppServer(msgExp);
            this.mdc.setRollbackOnly();
        }  finally {
            logger.info("Finished processing message with GUID: " + guid);
        }
    }

    private static String checkCustid(String custid) {
        return custid.startsWith("extmt_") ? custid.replace("extmt_", "") : custid;
    }


}
