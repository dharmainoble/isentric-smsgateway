//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.listener;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.repository.MessageServiceDao;
import com.isentric.bulkgateway.service.SmppMessageServiceBinder;
import com.isentric.bulkgateway.utility.SmsUtil;
import com.objectxp.msg.*;
import org.apache.log4j.Logger;

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
            MessageServiceDaoInterface messageServiceDao = new MessageServiceDao();
            if (event.getType() == 2) {
                SmsMessage sentMessage = (SmsMessage)event.getMessage();
                logger.debug("[" + this.listenerName + "]" + sentMessage.toString());
                messageServiceDao.updateSmppSent(sentMessage, "MESSAGE_SENT");
            } else if (event.getType() == 1) {
                Message msg = event.getMessage();
                messageServiceDao.insertMO(this.listenerName, (SmsMessage)msg);
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
                messageServiceDao.updateSmppSent(sentMessage, "MESSAGE_NOT_SENT");
            } else if (event.getType() != 4 && event.getType() != 10) {
                if (event.getType() == 11) {
                    logger.debug("[" + this.listenerName + "] incoming call: " + event.getSource());
                } else {
                    logger.debug("[" + this.listenerName + "] unknown event received: " + event.toString());
                }
            } else {
                logger.debug("[" + this.listenerName + "] event: " + event.toString());
                SmppMessageServiceBinder smppMessageServiceBinder = new SmppMessageServiceBinder();
                smppMessageServiceBinder.destroySmpp(this.listenerName);
                Thread.sleep(2000L);
                DBManagerDs dbManager = DBManagerDs.getManager("avatar");
                Connection con = dbManager.getConnection();

                try {
                    String configQuery = "select * from extmt.route_config where routeName='" + this.listenerName + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(configQuery);

                    while(rs.next()) {
                        String routeName = rs.getString("routeName");
                        String configType = rs.getString("configType");
                        String config = rs.getString("configFile");
                        if (configType.equals("smpp")) {
                            try {
                                smppMessageServiceBinder.setupSmpp(this.listenerName, config);
                                logger.debug("SMPP " + routeName + " initiation status: " + smppMessageServiceBinder.isInitialized(routeName));
                                logger.debug("SMPP " + routeName + " connection status: " + smppMessageServiceBinder.isConnected(routeName));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (configType.equals("ucp")) {
                            try {
                                smppMessageServiceBinder.setupUcp(this.listenerName, config);
                                logger.debug("SMPP " + routeName + " initiation status: " + smppMessageServiceBinder.isInitialized(routeName));
                                logger.debug("SMPP " + routeName + " connection status: " + smppMessageServiceBinder.isConnected(routeName));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    con.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            exception = e;
        } catch (MessageException e) {
            e.printStackTrace();
            exception = e;
        } catch (InterruptedException e) {
            e.printStackTrace();
            exception = e;
        } finally {
            if (exception != null) {
                SmsUtil.logException(exception);
            }

        }

    }
}
