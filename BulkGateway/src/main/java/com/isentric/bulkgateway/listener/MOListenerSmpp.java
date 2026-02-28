//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.listener;


import java.sql.SQLException;

import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.repository.MessageServiceDao;
import com.isentric.bulkgateway.service.SMSResponseWaitingService;
import com.isentric.bulkgateway.utility.SmsUtil;
import com.objectxp.msg.*;
import org.apache.log4j.Logger;
import com.isentric.bulkgateway.utility.SpringContextHolder;

public class MOListenerSmpp implements MessageEventListener {
    private static final Logger logger = LoggerManager.createLoggerPattern(MOListenerSmpp.class);
    private String listenerName = "";
    private SMSResponseWaitingService responseWaitingService;

    public MOListenerSmpp(String inListenerName) {
        this.listenerName = inListenerName;
        // Initialize response waiting service from Spring context
        try {
            if (SpringContextHolder.getContext() != null && SpringContextHolder.getContext().containsBean("smsResponseWaitingService")) {
                this.responseWaitingService = SpringContextHolder.getContext().getBean(SMSResponseWaitingService.class);
            }
        } catch (Exception e) {
            logger.warn("Failed to obtain SMSResponseWaitingService, response waiting feature will be disabled", e);
        }
    }

    public void handleMessageEvent(MessageEvent event) {
        logger.debug("Event Type : " + event.getType());
        logger.debug("All events received: " + event.toString());
        System.out.println("Event Type : " + event.getType());
        System.out.println("All events received: " + event.toString());
        System.out.println("listenerName - "+this.listenerName.toLowerCase());
        Exception exception = null;
        try {
            MessageServiceDao messageServiceDao = null;
            if (SpringContextHolder.getContext() != null && SpringContextHolder.getContext().containsBean("messageServiceDao")) {
                try {
                    messageServiceDao = SpringContextHolder.getContext().getBean(MessageServiceDao.class);
                } catch (Exception ex) {
                    logger.warn("Failed to obtain MessageServiceDao from Spring context, falling back to new instance", ex);
                }
            }
            if (messageServiceDao == null) {
                // fallback for non-Spring contexts
                messageServiceDao = new MessageServiceDao();
            }
            if (event.getType() == 2) {
                SmsMessage sentMessage = (SmsMessage)event.getMessage();
               logger.debug("[" + this.listenerName + "]" + sentMessage.toString());
                messageServiceDao.updateSmppSentMO(sentMessage, "MESSAGE_SENT");

                // Signal response waiting service if available
                if (responseWaitingService != null) {
                    String guid = (String) sentMessage.getProperty("SMPP_GUID");
                    String smppId = sentMessage.getID();
                    if (guid != null) {
                        SMSResponseWaitingService.SMSResponse response = new SMSResponseWaitingService.SMSResponse(guid);
                        response.smppId = smppId;
                        response.status = "MESSAGE_SENT";
                        response.eventType = 2;
                        responseWaitingService.signalResponse(guid, response);
                        logger.debug("Signaled SENT response for GUID: " + guid + ", SMPP ID: " + smppId);
                    }
                }
            } else if (event.getType() == 1) {
                Message msg = event.getMessage();
                //messageServiceDao.insertMO(this.listenerName, (SmsMessage)msg);
                logger.debug("GSM Message Received : " + msg.getMessage());
                logger.debug("GSM Message Received : " + msg.getClass().getName());
                logger.debug("GSM Message Received : " + msg.getSender());
            } else if (event.getType() == 5) {
                StatusReportMessage statusReportMessage = (StatusReportMessage)event.getMessage();
                String smppId = statusReportMessage.getID();
                String status = "";
                System.out.println(this.listenerName.toLowerCase());
                if (this.listenerName.toLowerCase().startsWith("ucp")) {
                    UcpStatus ucpStatus = (UcpStatus)statusReportMessage.getStatus();
                    status = SmsUtil.getUcpStatusMessageState(ucpStatus.getMessage());
                    if (!status.equalsIgnoreCase("STATE_ACCEPTED")) {
                        messageServiceDao.insertSmppDn(this.listenerName, statusReportMessage);
                    }
                } else if (this.listenerName.toLowerCase().startsWith("gsm")) {
                    logger.debug("[ GSM ] Receiving Status Message");
                    GsmStatus gsmStatus = (GsmStatus)statusReportMessage.getStatus();
                    status = SmsUtil.getSmppStatusMessageState(gsmStatus.getStatusValue());
                    messageServiceDao.insertSmppDn(this.listenerName, statusReportMessage);
                } else {
                    logger.debug("SMPP Status Event Type : " + event.getType());
                    logger.debug("SMPP Status events received: " + event.toString());
                    SmppStatus smppStatus = (SmppStatus)statusReportMessage.getStatus();
                    status = SmsUtil.getSmppStatusMessageState(smppStatus.getMessageState());
                    if (!status.equalsIgnoreCase("STATE_ACCEPTED")) {
                        messageServiceDao.insertSmppDn(this.listenerName, statusReportMessage);
                    }
                }

                logger.debug("[" + this.listenerName + "] recv status: " + statusReportMessage.toString());

                // Signal delivery response through waiting service
                if (responseWaitingService != null && smppId != null) {
                    SMSResponseWaitingService.SMSResponse response = new SMSResponseWaitingService.SMSResponse(smppId);
                    response.smppId = smppId;
                    response.status = status;
                    response.eventType = 5;
                    responseWaitingService.signalResponse(smppId, response);
                    logger.debug("Signaled DELIVERY response for SMPP ID: " + smppId + ", Status: " + status);
                }
            } else if (event.getType() == 6) {
                SmsMessage sentMessage = (SmsMessage)event.getMessage();
                logger.debug("[" + this.listenerName + "] recv not_sent: " + sentMessage.toString());
                messageServiceDao.updateSmppSentMO(sentMessage, "MESSAGE_NOT_SENT");

                // Signal failure response through waiting service
                if (responseWaitingService != null) {
                    String guid = (String) sentMessage.getProperty("SMPP_GUID");
                    if (guid != null) {
                        SMSResponseWaitingService.SMSResponse response = new SMSResponseWaitingService.SMSResponse(guid);
                        response.smppId = sentMessage.getID();
                        response.status = "MESSAGE_NOT_SENT";
                        response.error = "SMPP rejected the message";
                        response.eventType = 6;
                        responseWaitingService.signalResponse(guid, response);
                        logger.debug("Signaled FAILURE response for GUID: " + guid);
                    }
                }
            } else if (event.getType() != 4 && event.getType() != 10) {
                if (event.getType() == 11) {
                    logger.debug("[" + this.listenerName + "] incoming call: " + event.getSource());
                } else {
                    logger.debug("[" + this.listenerName + "] unknown event received: " + event.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            exception = e;
        }  finally {
            if (exception != null) {
                SmsUtil.logException(exception);
            }

        }

    }
}
