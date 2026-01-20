package com.isentric.smsserver.controller;

import com.isentric.smsserver.service.DeliveryNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DeliveryNotificationController {
    
    private final DeliveryNotificationService deliveryNotificationService;
    
    /**
     * Celcom Delivery Notification endpoint
     */
    @PostMapping("/receiveDN66399")
    public ResponseEntity<String> receiveCelcomDN(
            @RequestParam(required = false) String msgId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String msisdn,
            @RequestParam(required = false) String timestamp,
            HttpServletRequest request) {
        
        log.info("Celcom DN received - MsgID: {}, Status: {}, MSISDN: {}", msgId, status, msisdn);
        
        deliveryNotificationService.processDeliveryNotification(msgId, status, msisdn, "CELCOM", timestamp);
        
        return ResponseEntity.ok("DN received");
    }
    
    /**
     * Celcom SMP Delivery Notification endpoint
     */
    @PostMapping("/receiveSMPDN66399")
    public ResponseEntity<String> receiveCelcomSmpDN(
            @RequestParam(required = false) String msgId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String msisdn,
            HttpServletRequest request) {
        
        log.info("Celcom SMP DN received - MsgID: {}, Status: {}, MSISDN: {}", msgId, status, msisdn);
        
        deliveryNotificationService.processDeliveryNotification(msgId, status, msisdn, "CELCOM_SMP", null);
        
        return ResponseEntity.ok("DN received");
    }
    
    /**
     * Digi Delivery Notification endpoint
     */
    @PostMapping("/DigiDN")
    public ResponseEntity<String> receiveDigiDN(
            @RequestParam(required = false) String msgId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String msisdn,
            HttpServletRequest request) {
        
        log.info("Digi DN received - MsgID: {}, Status: {}, MSISDN: {}", msgId, status, msisdn);
        
        deliveryNotificationService.processDeliveryNotification(msgId, status, msisdn, "DIGI", null);
        
        return ResponseEntity.ok("DN received");
    }
    
    /**
     * SilverStreet Delivery Notification endpoint
     */
    @PostMapping("/SilverStreetDN")
    public ResponseEntity<String> receiveSilverStreetDN(
            @RequestParam(required = false) String msgId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String msisdn,
            HttpServletRequest request) {
        
        log.info("SilverStreet DN received - MsgID: {}, Status: {}, MSISDN: {}", msgId, status, msisdn);
        
        deliveryNotificationService.processDeliveryNotification(msgId, status, msisdn, "SILVERSTREET", null);
        
        return ResponseEntity.ok("DN received");
    }
}

