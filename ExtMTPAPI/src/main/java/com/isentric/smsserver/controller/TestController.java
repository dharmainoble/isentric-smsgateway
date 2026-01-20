package com.isentric.smsserver.controller;

import com.isentric.smsserver.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {
    
    private final SmsService smsService;
    
    /**
     * Health check / test endpoint
     */
    @GetMapping
    public ResponseEntity<String> test() {
        log.info("Test endpoint accessed");
        
        StringBuilder response = new StringBuilder();
        response.append("ExtMTPush SMS Gateway - Test Endpoint\n\n");
        response.append("Status: RUNNING\n");
        response.append("Timestamp: ").append(LocalDateTime.now()).append("\n");
        response.append("Version: 1.0.0\n");
        response.append("\nEndpoints:\n");
        response.append("- /extmtpush (SMS Push)\n");
        response.append("- /HLRLookup (HLR Lookup)\n");
        response.append("- /CheckSMSUserCredit (Credit Check)\n");
        response.append("- /ProcessModem (MO Messages)\n");
        response.append("- /receiveDN66399 (Celcom DN)\n");
        response.append("- /DigiDN (Digi DN)\n");
        response.append("- /SilverStreetDN (SilverStreet DN)\n");
        response.append("- /UpdateCacheServlet (Cache Management)\n");
        response.append("- /actuator/health (Health Check)\n");
        
        return ResponseEntity.ok(response.toString());
    }
    
    /**
     * Echo test endpoint
     */
    @GetMapping("/echo")
    public ResponseEntity<String> echo(@RequestParam(defaultValue = "Hello") String message) {
        return ResponseEntity.ok("Echo: " + message);
    }
}

