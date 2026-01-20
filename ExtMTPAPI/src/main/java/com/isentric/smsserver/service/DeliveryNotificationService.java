package com.isentric.smsserver.service;

import com.isentric.smsserver.model.avatar.ExtMtPushReceive;
import com.isentric.smsserver.repository.avatar.ExtMtPushReceiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryNotificationService {
    
    private final ExtMtPushReceiveRepository extMtPushReceiveRepository;
    
    /**
     * Process delivery notification from carrier
     */
    @Transactional("avatarTransactionManager")
    public void processDeliveryNotification(String msgId, String status, String msisdn, 
                                           String carrier, String timestamp) {
        try {
            log.info("Processing DN - MsgID: {}, Status: {}, Carrier: {}", msgId, status, carrier);
            
            if (msgId == null || msgId.isEmpty()) {
                log.warn("Invalid msgId received for DN");
                return;
            }
            
            // Find message by MTID
            Optional<ExtMtPushReceive> messageOpt = extMtPushReceiveRepository.findByMtid(msgId);
            
            if (messageOpt.isEmpty()) {
                log.warn("Message not found for MTID: {}", msgId);
                return;
            }
            
            ExtMtPushReceive message = messageOpt.get();
            
            // Map carrier status to standard status
            String deliveryStatus = mapDeliveryStatus(status, carrier);
            
            // Update delivery status
            message.setDeliveryStatus(deliveryStatus);
            message.setSentDate(LocalDateTime.now());
            
            extMtPushReceiveRepository.save(message);
            
            log.info("DN processed successfully - MTID: {}, Status: {}", msgId, deliveryStatus);
            
            // TODO: Send callback to customer if required
            // sendCustomerCallback(message);
            
        } catch (Exception e) {
            log.error("Error processing delivery notification for msgId: {}", msgId, e);
        }
    }
    
    /**
     * Map carrier-specific status to standard status
     */
    private String mapDeliveryStatus(String status, String carrier) {
        if (status == null) {
            return "UNKNOWN";
        }
        
        // Normalize status
        String normalizedStatus = status.toUpperCase().trim();
        
        // Common mappings
        return switch (normalizedStatus) {
            case "DELIVERED", "DELIVRD", "SUCCESS", "1" -> "DELIVERED";
            case "FAILED", "FAIL", "UNDELIV", "2" -> "FAILED";
            case "EXPIRED", "3" -> "EXPIRED";
            case "REJECTED", "REJECTD", "4" -> "REJECTED";
            case "PENDING", "5" -> "PENDING";
            default -> "UNKNOWN_" + normalizedStatus;
        };
    }
}

