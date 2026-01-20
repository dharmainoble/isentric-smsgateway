package com.isentric.smsserver.jms;

import com.isentric.smsserver.model.avatar.ExtMtPushReceive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.jms.enabled", havingValue = "true", matchIfMissing = true)
public class SmsMessageListener {
    
    /**
     * Listen to incoming SMS queue
     */
    @JmsListener(destination = "${sms.queue.incoming}")
    public void receiveIncomingMessage(ExtMtPushReceive message) {
        try {
            log.info("Received incoming SMS - MTID: {}, MSISDN: {}", 
                     message.getMtid(), message.getRmsisdn());
            
            // TODO: Process incoming message
            // This would handle MO (Mobile Originated) messages
            
        } catch (Exception e) {
            log.error("Error processing incoming message", e);
        }
    }
    
    /**
     * Listen to send SMS queue
     */
    @JmsListener(destination = "${sms.queue.send}")
    public void receiveSendMessage(ExtMtPushReceive message) {
        try {
            log.info("Processing send SMS - MTID: {}, MSISDN: {}, Customer: {}", 
                     message.getMtid(), message.getRmsisdn(), message.getCustid());
            
            // TODO: Send SMS via SMPP or HTTP gateway
            // This would integrate with actual SMS gateway/SMPP connection
            
            // Steps:
            // 1. Route to appropriate carrier based on MSISDN prefix
            // 2. Format message according to product type
            // 3. Send via SMPP or HTTP API
            // 4. Update status in database
            
            log.info("SMS sent successfully - MTID: {}", message.getMtid());
            
        } catch (Exception e) {
            log.error("Error sending SMS - MTID: {}", message.getMtid(), e);
            // TODO: Update status as failed in database
        }
    }
    
    /**
     * Listen to outgoing SMS queue (for delivery reports)
     */
    @JmsListener(destination = "${sms.queue.outgoing}")
    public void receiveOutgoingMessage(ExtMtPushReceive message) {
        try {
            log.info("Received outgoing SMS status - MTID: {}, Status: {}", 
                     message.getMtid(), message.getDeliveryStatus());
            
            // TODO: Process delivery reports
            // This would handle DN (Delivery Notification) messages
            
        } catch (Exception e) {
            log.error("Error processing outgoing message", e);
        }
    }
}

