package com.isentric.bulkgateway.service;

import com.isentric.bulkgateway.bg.model.SMSMessageSmpp;
import com.objectxp.msg.MessageException;
import com.objectxp.msg.SmsMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isentric.bulkgateway.manager.LoggerManager;
import com.isentric.bulkgateway.utility.StringUtil;

/**
 * Example service showing how to capture SMPP responses synchronously
 */
@Service
public class SMPPResponseCapturingService {

    private static final Logger logger = LoggerManager.createLoggerPattern(SMPPResponseCapturingService.class);

    @Autowired
    private SMSResponseWaitingService responseWaitingService;

    @Autowired
    private SmppMessageServiceBinder smppMessageServiceBinder;

    /**
     * Send message and wait for immediate SMPP ACK (MESSAGE_SENT event)
     *
     * @param smppName Name of the SMPP connection (e.g., "smpp1")
     * @param credit Credit to use
     * @param smsMessage Message to send
     * @param timeoutSeconds How long to wait for response
     * @return Response object or null if timeout
     */
    public SMSResponseWaitingService.SMSResponse sendAndWaitForAck(
            String smppName,
            String credit,
            SMSMessageSmpp smsMessage,
            int timeoutSeconds) {

        String guid = smsMessage.getGuid();
        logger.info("Sending message GUID: " + guid);

        try {
            // Register the message as pending
            responseWaitingService.registerPendingMessage(guid);

            // Send the message
            smppMessageServiceBinder.sendSmpp(smppName, credit, smsMessage);

            // Wait for ACK (MESSAGE_SENT event from SMPP server)
            SMSResponseWaitingService.SMSResponse response =
                responseWaitingService.waitForResponse(guid, timeoutSeconds);

            if (response != null) {
                logger.info("SMPP ACK received for GUID: " + guid +
                           ", SMPP ID: " + response.smppId +
                           ", Status: " + response.status);
                return response;
            } else {
                logger.warn("SMPP ACK timeout for GUID: " + guid +
                           " after " + timeoutSeconds + " seconds");
                return null;
            }

        } catch (Exception e) {
            logger.error("Error sending message GUID: " + guid, e);
            return null;
        }
    }

    /**
     * Send message and wait for delivery report
     *
     * @param smppName Name of the SMPP connection
     * @param credit Credit to use
     * @param smsMessage Message to send
     * @param timeoutSeconds How long to wait for delivery report
     * @return Response object with delivery status or null if timeout
     */
    public SMSResponseWaitingService.SMSResponse sendAndWaitForDelivery(
            String smppName,
            String credit,
            SMSMessageSmpp smsMessage,
            int timeoutSeconds) {

        String guid = smsMessage.getGuid();
        logger.info("Sending message GUID: " + guid + " and waiting for delivery report");

        try {
            // Register the message as pending
            responseWaitingService.registerPendingMessage(guid);

            // Send the message
            smppMessageServiceBinder.sendSmpp(smppName, credit, smsMessage);

            // Wait for delivery report (may take longer than ACK)
            SMSResponseWaitingService.SMSResponse response =
                responseWaitingService.waitForDeliveryReport(guid, timeoutSeconds);

            if (response != null && response.status != null) {
                logger.info("Delivery status received for GUID: " + guid +
                           ", Status: " + response.status +
                           ", SMPP ID: " + response.smppId);
                return response;
            } else {
                logger.warn("Delivery report timeout for GUID: " + guid +
                           " after " + timeoutSeconds + " seconds");
                return null;
            }

        } catch (Exception e) {
            logger.error("Error sending message GUID: " + guid, e);
            return null;
        } finally {
            // Optional: clean up after processing
            responseWaitingService.cleanup(guid);
        }
    }

    /**
     * Send message without waiting (fire-and-forget)
     * Use database queries to check status later
     *
     * @param smppName Name of the SMPP connection
     * @param credit Credit to use
     * @param smsMessage Message to send
     * @return GUID of the sent message for later reference
     */
    public String sendAsynchronous(
            String smppName,
            String credit,
            SMSMessageSmpp smsMessage) {

        String guid = smsMessage.getGuid();
        logger.info("Sending message asynchronously, GUID: " + guid);

        try {
            smppMessageServiceBinder.sendSmpp(smppName, credit, smsMessage);
            logger.info("Message sent, GUID: " + guid + " - check database for response");
            return guid;
        } catch (Exception e) {
            logger.error("Error sending message GUID: " + guid, e);
            return null;
        }
    }

    /**
     * Get pending message count
     * Useful for monitoring
     */
    public int getPendingMessageCount() {
        return responseWaitingService.getPendingMessageCount();
    }

    /**
     * Clean up old responses
     * Call periodically to free memory
     */
    public void cleanupOldResponses(int olderThanMinutes) {
        logger.info("Cleaning up responses older than " + olderThanMinutes + " minutes");
        responseWaitingService.cleanupOldResponses(olderThanMinutes);
    }
}

