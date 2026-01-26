//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.listener;


import java.sql.SQLException;

import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.repository.MessageServiceDao;
import com.isentric.bulkgateway.utility.SmsUtil;
import com.objectxp.msg.*;
import org.apache.log4j.Logger;
import com.isentric.bulkgateway.utility.SpringContextHolder;

public class MOListenerSmpp implements MessageEventListener {
    private static final Logger logger = LoggerManager.createLoggerPattern(MOListenerSmpp.class);
    private String listenerName = "";

    public MOListenerSmpp(String inListenerName) {
        this.listenerName = inListenerName;
    }

    public void handleMessageEvent(MessageEvent event) {
        logger.debug("Event Type : " + event.getType());
        logger.debug("All events received: " + event.toString());
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
                //SMSMessageSmpp sentMessage = (SMSMessageSmpp)event.getMessage();
               // logger.debug("[" + this.listenerName + "]" + sentMessage.toString());
                //messageServiceDao.updateSmppSent(sentMessage, "MESSAGE_SENT");
            } else if (event.getType() == 1) {
                Message msg = event.getMessage();
                //messageServiceDao.insertMO(this.listenerName, (SmsMessage)msg);
                logger.debug("GSM Message Received : " + msg.getMessage());
                logger.debug("GSM Message Received : " + msg.getClass().getName());
                logger.debug("GSM Message Received : " + msg.getSender());
            } else if (event.getType() == 5) {
                StatusReportMessage statusReportMessage = (StatusReportMessage)event.getMessage();
                if (this.listenerName.toLowerCase().startsWith("ucp")) {
                    UcpStatus ucpStatus = (UcpStatus)statusReportMessage.getStatus();
                    if (!SmsUtil.getUcpStatusMessageState(ucpStatus.getMessage()).equalsIgnoreCase("STATE_ACCEPTED")) {
                        messageServiceDao.insertSmppDn(this.listenerName, statusReportMessage);
                    }
                } else if (this.listenerName.toLowerCase().startsWith("gsm")) {
                    logger.debug("[ GSM ] Receiving Status Message");
                    GsmStatus gsmStatus = (GsmStatus)statusReportMessage.getStatus();
                    messageServiceDao.insertSmppDn(this.listenerName, statusReportMessage);
                } else {
                    logger.debug("SMPP Status Event Type : " + event.getType());
                    logger.debug("SMPP Status events received: " + event.toString());
                    SmppStatus smppStatus = (SmppStatus)statusReportMessage.getStatus();
                    if (!SmsUtil.getSmppStatusMessageState(smppStatus.getMessageState()).equalsIgnoreCase("STATE_ACCEPTED")) {
                        messageServiceDao.insertSmppDn(this.listenerName, statusReportMessage);
                    }
                }

                logger.debug("[" + this.listenerName + "] recv status: " + statusReportMessage.toString());
            } else if (event.getType() == 6) {
                SmsMessage sentMessage = (SmsMessage)event.getMessage();
                logger.debug("[" + this.listenerName + "] recv not_sent: " + sentMessage.toString());
                //messageServiceDao.updateSmppSent(sentMessage, "MESSAGE_NOT_SENT");
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
