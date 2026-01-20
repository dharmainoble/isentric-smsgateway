package com.isentric.smsserver.controller;

import com.isentric.smsserver.object.ExtSMSObject;
import com.isentric.smsserver.object.SMSMessageExtMTObject;
import com.isentric.smsserver.service.ExtMTPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class ExtMTPushController {

    private static final Logger logger = LoggerFactory.getLogger(ExtMTPushController.class);

    private final ExtMTPushService extMTPushService;

    @Autowired
    public ExtMTPushController(ExtMTPushService extMTPushService) {
        this.extMTPushService = extMTPushService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendSMS(@RequestBody SMSMessageExtMTObject smsMessage) {
        logger.info("Received SMS send request for: {}", smsMessage.getRmsisdn());

        Map<String, Object> response = new HashMap<>();
        String result = extMTPushService.sendSMS(smsMessage);

        if (result.startsWith("SUCCESS")) {
            response.put("status", "success");
            response.put("message", "SMS sent successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", result);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/bulk/send")
    public ResponseEntity<Map<String, Object>> sendBulkSMS(@RequestBody ExtSMSObject extSmsObject) {
        logger.info("Received bulk SMS send request for: {}", extSmsObject.getRmsisdn());

        Map<String, Object> response = new HashMap<>();
        String result = extMTPushService.sendBulkSMS(extSmsObject);

        if (result.startsWith("SUCCESS")) {
            response.put("status", "success");
            response.put("message", "Bulk SMS sent successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", result);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status/{mtid}")
    public ResponseEntity<Map<String, Object>> checkDeliveryStatus(@PathVariable String mtid) {
        logger.info("Checking delivery status for: {}", mtid);

        Map<String, Object> response = new HashMap<>();
        String status = extMTPushService.checkDeliveryStatus(mtid);

        response.put("mtid", mtid);
        response.put("status", status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/credit/{custid}")
    public ResponseEntity<Map<String, Object>> getUserCredit(@PathVariable String custid) {
        logger.info("Getting credit for customer: {}", custid);

        Map<String, Object> response = new HashMap<>();
        double credit = extMTPushService.getUserCredit(custid);

        response.put("custid", custid);
        response.put("credit", credit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/credit/deduct")
    public ResponseEntity<Map<String, Object>> deductCredit(@RequestParam String custid, @RequestParam double amount) {
        logger.info("Deducting credit for customer: {}, amount: {}", custid, amount);

        Map<String, Object> response = new HashMap<>();
        boolean success = extMTPushService.deductCredit(custid, amount);

        response.put("custid", custid);
        response.put("amount", amount);
        response.put("success", success);

        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "ExtMTPush");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}

